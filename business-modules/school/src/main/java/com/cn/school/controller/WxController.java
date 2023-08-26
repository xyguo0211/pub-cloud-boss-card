package com.cn.school.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.config.TimingLog;
import com.cn.auth.config.jwt.TokenProvider;
import com.cn.auth.entity.User;
import com.cn.school.config.Constant;
import com.cn.school.config.WxPayConfig;
import com.cn.school.entity.TripOrderDo;
import com.cn.school.entity.UserDo;
import com.cn.school.entity.dto.UnlimitedQRCodeParam;
import com.cn.school.service.impl.TripOrderServiceImpl;
import com.cn.school.service.impl.UserServiceImpl;
import com.cn.school.util.WXPayUtil;
import com.cn.school.util.WxPayRequstUtil;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.redis.util.RedisCache;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/school/wx")
@RefreshScope
public class WxController extends BaseController {

    private Logger log= LoggerFactory.getLogger("wxLogger");

    @Value("${short_token_redis_cache_time}")
    private  Long short_token_redis_cache_time ;

    @Autowired
    private RedisCache redisCache;



    //微信Scope，固定snsapi_login
    private String wxScope = "snsapi_login";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private WxPayConfig wxPayConfig;

    @Resource
    private TokenProvider tokenProvider;

    @Autowired
    private TripOrderServiceImpl tripOrderService;



    /**
     * 扫码成功回调
     */
    @RequestMapping(value = "/wxCallback", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult wxCallback(@RequestParam String code)  {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",200);
        if (code == null || "".equals(code)) {
            return error("必传参数code为空！");
        }

        //1.通过code获取access_token
        //String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";
        url = url.replace("APPID", wxPayConfig.getAppid()).replace("SECRET", wxPayConfig.getWxAppSecret()).replace("CODE", code);
        ResponseEntity<String> tokenData = restTemplate.getForEntity(url, String.class);
        String tokenInfoStr = tokenData.getBody();

        JSONObject tokenInfoObject = JSONObject.parseObject(tokenInfoStr);
        log.info("tokenInfoObject:{}", tokenInfoObject);
        String openid = tokenInfoObject.getString("openid");
        if (openid == null || "".equals(tokenInfoObject.getString("openid"))) {
            log.error("用code获取openid失败");
            return  error("用code获取openid失败！");
        }
        String sesssoin_key = tokenInfoObject.getString("sesssoin_key");
        /**
         * {
         * 	"openid": "oVLC_6lTZxSWkoZA9L1povjQiplA",
         * 	"nickname": "等疯",
         * 	"sex": 0,
         * 	"language": "",
         * 	"city": "",
         * 	"province": "",
         * 	"country": "",
         * 	"headimgurl": "https:\/\/thirdwx.qlogo.cn\/mmopen\/vi_32\/VTmtGJfvcbax9LB3OPSMURhn6jJ97zibC8Ihicsp9nEwicFZwsPeqprkJmTfQ1wLaJRck9O1icIiatmVyWpibFbg8G4w\/132",
         * 	"privilege": [],
         * 	"unionid": "omvh-6e_lb4ZVFoqTKpnOspgSZIY"
         * }
         */


        /**
         * 根据openid查询平台用户：
         *  1. 如果没有查询到用户，则将openid与平台用户绑定，或者注册新账户
         *  2. 如果查询到用户，则调用本地登录方法
         */
        Date date=new Date();
        QueryWrapper<UserDo> wq_user=new QueryWrapper<>();
        wq_user.eq("openid",openid);
        UserDo userDo = userServiceImpl.getOne(wq_user);
        String token=null;
        if(userDo==null){
           //说明是新用户，需要榜单手机
            userDo=new UserDo();
            userDo.setOpenid(openid);
            userDo.setWxunionid(sesssoin_key);
            userDo.setIsDelete(1);
            userServiceImpl.save(userDo);
            token = createToken(userDo);
        }else{
            Integer isDelete = userDo.getIsDelete();
            if(0==isDelete){
                //被禁用的用户
                jsonObject.put("code",400);
                jsonObject.put("msg","用户被禁用");
                return success(jsonObject);
            }
            //说明是老用户
            if(StringUtils.isNotBlank(sesssoin_key)){
                userDo.setWxunionid(sesssoin_key);
            }
           token = createToken(userDo);
        }
        jsonObject.put("token",token);
        jsonObject.put("user",userDo);
        return success(jsonObject);
    }

    public String createToken(UserDo userDo){
        User mUser=new User();
        /*mUser.setLoginName(userDo.getNickName());*/
        mUser.setId(userDo.getId());
        mUser.setPassword(userDo.getOpenid());
        String jwt ="BearerSchool" +  tokenProvider.createToken(mUser);
        redisCache.putCacheWithExpireTime(jwt,mUser,short_token_redis_cache_time);
        return jwt;
    }

    @RequestMapping(value = "/wxPayNotify", method = RequestMethod.POST)
    public String wxNotify(HttpServletRequest request){
        //用于处理结束后返回的xml
        String resXml = "";
        String key = "&key="+ Constant.privateKey;
        try {
            InputStream in = request.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len = 0;
            byte[] b = new byte[1024];
            while((len = in.read(b)) != -1){
                out.write(b, 0, len);
            }
            out.close();
            in.close();
            //将流 转为字符串
            String result = new String(out.toByteArray(), "utf-8");
            Map<String, String> map = WxPayRequstUtil.getNotifyUrl(result);
            String return_code = map.get("return_code").toString().toUpperCase();
            if(return_code.equals("SUCCESS")){
                //进行签名验证，看是否是从微信发送过来的，防止资金被盗
                if(WxPayRequstUtil.verifyWeixinNotify(map, key)){
                    //签名验证成功后按照微信要求返回的xml
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    return resXml;
                }
            }else{
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[sign check error]]></return_msg>" + "</xml> ";
                return resXml;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                + "<return_msg><![CDATA[xml error]]></return_msg>" + "</xml> ";
        return resXml;

    }


    @TimingLog
    @RequestMapping(value = "/getImageByte", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getImageByte(@RequestParam Integer orderId){
        try{
            UnlimitedQRCodeParam body=new UnlimitedQRCodeParam();
            body.setPage("main/test");
            body.setScene(orderId+"");
            body.setCheckPath(false);
            byte[] imageByte = tripOrderService.getImageByte(body);
            HttpServletResponse response = getResponse();
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(imageByte);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

}
