package com.cn.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.config.Constant;
import com.cn.auth.config.jwt.TokenProvider;
import com.cn.auth.entity.User;
import com.cn.user.entity.LoginVM;
import com.cn.user.service.impl.SysRoleMenuServiceImpl;
import com.cn.user.service.impl.SysUserServiceImpl;
import com.pub.redis.util.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SysUserServiceImpl sysUserServiceImpl;
    @Autowired
    private SysRoleMenuServiceImpl sysRoleMenuServiceImpl;

    @Autowired
    private TokenProvider tokenProvider;

    /**
     * 登陆
     *
     * @return
     */
    /*@Encrypt*/


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject login(@RequestBody LoginVM loginVM) throws ParseException {
        JSONObject js=new JSONObject();
        String encodePassword ="";
        if(StringUtils.isNotBlank(loginVM.getPassword())&&StringUtils.isNotBlank(loginVM.getUsername())){
            encodePassword = loginVM.getPassword();
        }else{
            js.put("code",0);
            js.put("msg","不能输入空的账户或密码!");
            return js;
        }
        QueryWrapper<User> wq=new QueryWrapper<>();
        wq.eq("login_name",loginVM.getUsername());
        User mUser = sysUserServiceImpl.getOne(wq);
        if(mUser==null){
            js.put("code",0);
            js.put("msg","用户不存在!");
            return js;

        }
        String errKey= Constant.ERRO_PASSWORD+"-"+loginVM.getUsername();
        String errorTimes= redisCache.getStringCache(errKey);
        //密码输入错误超过5次 会被锁定账号，所以，如果是这种情况需要先解除账号
        if(StringUtils.isBlank(errorTimes)&&mUser.getUserType()!=1){
            //锁定解除
            mUser.setUserType(1);
            sysUserServiceImpl.updateById(mUser);
        }
        if(StringUtils.isNotBlank(errorTimes)){
            if(Integer.parseInt(errorTimes)==5){
                js.put("code",0);
                js.put("msg","密码输入错误五次账户已被锁定,请稍后登录!");
                return js;
            }
        }
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();
        Map<String, String> permissions = new HashMap<String, String>();
        if (mUser.getStatus() == Constant.MUSER_STATUS_YES) {
            if (loginVM.getPassword().equals( mUser.getPassword())) {
                //账号密码登录，密码正确，缓存用户权限和用户
                mUser.setLoginDate(new Date());
                sysUserServiceImpl.updateById(mUser);
                // 超级用户在一下方法内已经处理了
                if (mUser.getStatus().equals(1)) {
                    JSONObject menuByUserId = sysRoleMenuServiceImpl.getMenuByUserId(mUser.getId());//获取用户在当前系统的菜单
                    List<JSONObject> menus = (List<JSONObject>) menuByUserId.get("menus");
                    permissions = (Map<String, String>) menuByUserId.get("permissions");
                    js.put("menus",menus);
                    js.put("permissions",permissions);
                    logger.info("menus>>>"+menus);
                    if (menus == null||menus.size()==0) {
                        js.put("code",0);
                        js.put("msg","当前用户没有分配菜单权限!");
                        return js;
                    }
                }
                if (Constant.SYSTEM_SUPER_USER.equals(mUser.getId())) {
                    permissions.put("*", "*");//这里表示超级用户可显示所有菜单
                } else {
                    Set<String> keys = permissions.keySet().stream().filter(Objects::nonNull).collect(Collectors.toSet());
                    permissions.keySet().retainAll(keys);
                    logger.info("permissions>>>" + permissions);
                }
                //1 表示2.0系统登录 2表示标准化服务平台登录
                String key;
                if(loginVM.getType() == 2){
                    key=Constant.REDIS_PERMISSION_CACHE_ID_ASSESSMENT + mUser.getId();
                }else{
                    key= Constant.REDIS_PERMISSION_CACHE_ID_ + mUser.getId();
                }
                redisCache.putCache(Constant.REDIS_PERMISSION_CACHE_KEY+key, permissions);
                request.getSession().setAttribute(Constant.MANAGE_SESSION_USER, mUser);
                redisCache.putCache(Constant.REDIS_USER_CACHE_KEY+Constant.REDIS_USER_CACHE_ID_ + mUser.getId(), mUser);
                String jwt = tokenProvider.createToken(mUser);
                response.addHeader(TokenProvider.AUTHORIZATION_HEADER, "Bearer " + jwt);

                js.put("user",mUser);
                return  js;
            } else {
                if(StringUtils.isBlank(errorTimes)){
                    redisCache.putCacheWithExpireTime(errKey,"1",30);
                    js.put("code",0);
                    js.put("msg","密码错误!");
                    return js;
                }else {
                    if(Integer.parseInt(errorTimes)<5){
                        redisCache.putCacheWithExpireTime(errKey,String.valueOf(Integer.parseInt(errorTimes)+1),30);;
                        js.put("code",0);
                        js.put("msg","密码错误!");
                        return js;
                    }else{
                        // RedisCacheUtils.removeKey(errKey);
                        js.put("code",0);
                        js.put("msg","单次登陆失败已超过5次,请半个小时后操作!");
                        //账户锁定 无法操作
                        mUser.setUserType(0);
                        sysUserServiceImpl.updateById(mUser);
                        return js;
                    }

                }
            }
        } else {
            js.put("code",0);
            js.put("msg","用户已被禁用,请联系管理员!");
            return js;
        }
    }
    //获取用户真实ip
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public HttpServletRequest getRequest(){
        return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
    public HttpServletResponse getResponse(){
        return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }
}
