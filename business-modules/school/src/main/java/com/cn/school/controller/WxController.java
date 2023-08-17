package com.cn.school.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.config.jwt.TokenProvider;
import com.cn.auth.entity.User;
import com.cn.school.config.WxPayConfig;
import com.cn.school.entity.UserDo;
import com.cn.school.service.impl.UserServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.redis.util.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;

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
    private com.cn.school.config.WxPayConfig wxPayConfig;

    @Resource
    private TokenProvider tokenProvider;

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
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        url = url.replace("APPID", wxPayConfig.getAppid()).replace("SECRET", wxPayConfig.getWxAppSecret()).replace("CODE", code);
        ResponseEntity<String> tokenData = restTemplate.getForEntity(url, String.class);
        String tokenInfoStr = tokenData.getBody();

        JSONObject tokenInfoObject = JSONObject.parseObject(tokenInfoStr);
        log.info("tokenInfoObject:{}", tokenInfoObject);
        if (tokenInfoObject.getString("access_token") == null || "".equals(tokenInfoObject.getString("access_token"))) {
            log.info("用code获取access_token失败");
            return  error("用code获取access_token失败！");
        }

        //2.通过access_token和openid获取用户信息
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
        userInfoUrl = userInfoUrl.replace("ACCESS_TOKEN", tokenInfoObject.getString("access_token")).replace("OPENID", tokenInfoObject.getString("openid"));
        ResponseEntity<String> userData = restTemplate.getForEntity(userInfoUrl, String.class);
        String userInfoStr = userData.getBody();
        JSONObject userInfoObject = JSONObject.parseObject(userInfoStr);
        log.info("userInfoObject:{}", userInfoStr);
        String wx_openid = userInfoObject.getString("openid");
        if (StringUtils.isBlank(wx_openid)) {
            log.info("获取用户信息失败");
            return  error("获取微信用户信息失败！");
        }

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

        String wx_unionid = userInfoObject.getString("unionid");
        String wx_nickname = userInfoObject.getString("nickname");

        /**
         * 根据openid查询平台用户：
         *  1. 如果没有查询到用户，则将openid与平台用户绑定，或者注册新账户
         *  2. 如果查询到用户，则调用本地登录方法
         */
        Date date=new Date();
        QueryWrapper<UserDo> wq_user=new QueryWrapper<>();
        wq_user.eq("open_id",wx_openid);
        UserDo userDo = userServiceImpl.getOne(wq_user);
        String token=null;
        if(userDo==null){
           //说明是新用户，需要榜单手机
            userDo=new UserDo();
            userDo.setOpenid(wx_openid);
            userDo.setNickName(wx_nickname);
            userDo.setWxunionid(wx_unionid);
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
            if(StringUtils.isNotBlank(wx_unionid)){
                userDo.setWxunionid(wx_unionid);
            }
           if(StringUtils.isNotBlank(wx_nickname)){
               userDo.setNickName(wx_nickname);
           }
           token = createToken(userDo);
        }
        jsonObject.put("token",token);
        jsonObject.put("user",userDo);
        return success(jsonObject);
    }

    public String createToken(UserDo userDo){
        User mUser=new User();
        mUser.setLoginName(userDo.getNickName());
        mUser.setId(userDo.getId());
        mUser.setPassword(userDo.getOpenid());
        String jwt ="BearerSchool" +  tokenProvider.createToken(mUser);
        redisCache.putCacheWithExpireTime(jwt,mUser,short_token_redis_cache_time);
        return jwt;
    }



}
