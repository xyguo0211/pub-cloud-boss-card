package com.cn.school.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.config.TimingLog;
import com.cn.auth.config.jwt.TokenProvider;
import com.cn.auth.entity.User;
import com.cn.school.config.Constant;
import com.cn.school.config.WxPayConfig;
import com.cn.school.entity.TripOrderDo;
import com.cn.school.entity.UserDo;
import com.cn.school.entity.dto.UnlimitedQRCodeParam;
import com.cn.school.service.impl.SysDataDictionaryServiceImpl;
import com.cn.school.service.impl.TripOrderServiceImpl;
import com.cn.school.service.impl.UserServiceImpl;
import com.cn.school.util.WXPayUtil;
import com.cn.school.util.WxPayRequstUtil;
import com.pub.core.exception.BusinessException;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.utils.CalculateUtil;
import com.pub.redis.util.RedisCache;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
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
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/school/wx")
@RefreshScope
public class WxController extends BaseController {

    private Logger log= LoggerFactory.getLogger("wxLogger");
    private Logger callBackLog= LoggerFactory.getLogger("callBackLog");
    private Logger loginLog= LoggerFactory.getLogger("loginLog");

    @Value("${short_token_redis_cache_time}")
    private  Long short_token_redis_cache_time ;
    @Value("${pagePath}")
    private  String pagePath ;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private TripOrderServiceImpl tripOrderServiceImpl;



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

    @Autowired
    private SysDataDictionaryServiceImpl sysDataDictionaryServiceImpl;

    /**
     * 扫码成功回调
     */
    @RequestMapping(value = "/wxCallback", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult wxCallback(@RequestParam String code,@RequestParam(required = false)String invitationOpenid)  {
        loginLog.info("登录的opendId={},邀请码={}",code,invitationOpenid);
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
           //说明是新用户，需要绑定手机
            userDo=new UserDo();
            userDo.setOpenid(openid);
            userDo.setWxunionid(sesssoin_key);
            userDo.setIsDelete(9);
            userDo.setRoleId(3);
            userDo.setNoticeStatus(0);
            userDo.setCreateTime(new Date());
            userDo.setInvitationOpenid(invitationOpenid);
            if(StringUtils.isNotBlank(invitationOpenid)){
                userDo.setIntegral("0");
            }
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
        String jwt ="BearerSchool" +  tokenProvider.createTokenNewSchool(mUser);
        redisCache.putCacheWithExpireTime(jwt,mUser,short_token_redis_cache_time);
        return jwt;
    }

    @TimingLog
    @RequestMapping(value = "/getImageByte", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getImageByte(@RequestParam Integer id){
        try{
            TripOrderDo tripOrderDo = tripOrderService.getById(id);
            Integer status = tripOrderDo.getStatus();
            if(status!=Constant.OrderStatus.SUCESS){
                return AjaxResult.error("未支付或者已退票!");
            }
            Integer num = tripOrderDo.getNum();
            Integer onCarStatus = tripOrderDo.getOnCarStatus();
            if(onCarStatus>=num){
                return AjaxResult.error("该车票已被核销完成!");
            }
            UnlimitedQRCodeParam body=new UnlimitedQRCodeParam();
            body.setPage(pagePath);
            body.setScene(id+"");
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

    @TimingLog
    @RequestMapping(value = "/payCallBack", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> payCallBack(@RequestBody JSONObject jsonObject) {
        callBackLog.info("支付回调请求参数{}"+jsonObject.toJSONString());
        try {
            String key = wxPayConfig.getApiV3Key();
            String json = jsonObject.toString();
            String associated_data = (String) JSONUtil.getByPath(JSONUtil.parse(json), "resource.associated_data");
            String ciphertext = (String) JSONUtil.getByPath(JSONUtil.parse(json), "resource.ciphertext");
            String nonce = (String) JSONUtil.getByPath(JSONUtil.parse(json), "resource.nonce");

            String decryptData = new AesUtil(key.getBytes(StandardCharsets.UTF_8)).decryptToString(associated_data.getBytes(StandardCharsets.UTF_8), nonce.getBytes(StandardCharsets.UTF_8), ciphertext);
            //验签成功
            JSONObject decryptDataObj = JSONObject.parseObject(decryptData, JSONObject.class);
            callBackLog.info("支付回调解析后请求参数参数{}"+decryptDataObj.toJSONString());
            //decryptDataObj 为解码后的obj，其内容如下。之后便是验签成功后的业务处理
            //{
            //	"sp_appid": "wx8888888888888888",
            //	"sp_mchid": "1230000109",
            //	"sub_appid": "wxd678efh567hg6999",
            //	"sub_mchid": "1900000109",
            //	"out_trade_no": "1217752501201407033233368018",
            //	"trade_state_desc": "支付成功",
            //	"trade_type": "MICROPAY",
            //	"attach": "自定义数据",
            //	"transaction_id": "1217752501201407033233368018",
            //	"trade_state": "SUCCESS",
            //	"bank_type": "CMC",
            //	"success_time": "2018-06-08T10:34:56+08:00",
            //    ...
            //	"payer": {
            //		"openid": "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o"
            //	},
            //	"scene_info": {
            //		"device_id": "013467007045764"
            //	}
            //}
            String out_trade_no = decryptDataObj.getString("out_trade_no");
            String trade_state_desc = decryptDataObj.getString("trade_state_desc");
            QueryWrapper<TripOrderDo> wq=new QueryWrapper<>();
            wq.eq("order_id",out_trade_no);
            TripOrderDo tripOrderDo = tripOrderServiceImpl.getOne(wq);
            if("支付成功".equals(trade_state_desc)){
                tripOrderDo.setStatus(1);
                tripOrderServiceImpl.setInvitationFee(tripOrderDo);

            }else{
                tripOrderDo.setStatus(-1);
            }
            tripOrderServiceImpl.wxPayNotify(tripOrderDo);
        }catch (Exception e){
            e.printStackTrace();
            callBackLog.error("支付回调处理异常{}"+jsonObject.toJSONString());
        }

        Map<String, String> res = new HashMap<>();
        res.put("code", "SUCCESS");
        res.put("message", "成功");
        return res;
    }


}
