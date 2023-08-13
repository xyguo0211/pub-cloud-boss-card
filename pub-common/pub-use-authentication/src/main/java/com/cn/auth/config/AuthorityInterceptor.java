package com.cn.auth.config;

import com.alibaba.fastjson.JSONObject;
import com.cn.auth.config.jwt.TokenProvider;
import com.cn.auth.entity.ResultMessageConstants;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.pub.core.common.OfflineConstants;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.utils.DateUtils;
import com.pub.redis.util.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

public class AuthorityInterceptor extends HandlerInterceptorAdapter {

    protected Logger logger = LoggerFactory.getLogger(AuthorityInterceptor.class);
    private TokenProvider tokenProvider;
    private UserContext userContext;
    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("StopWatch-StartTime");

    /**
     * 注意这里不能够使用Autowired，因为使用的时候是用new的
     * @param tokenProvider
     * @param redisCache
     */
    /*@Autowired
    private RedisCache redisCache;*/

    private RedisCache redisCache;

    public AuthorityInterceptor(TokenProvider tokenProvider,RedisCache redisCache) {
        this.tokenProvider = tokenProvider;
        this.redisCache=redisCache;
    }

    private void responseMessage(HttpServletResponse response, Object obj) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter pw = response.getWriter();
        pw.write(JSONObject.toJSONString(obj));
        pw.close();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        long beginTime = System.currentTimeMillis();//1、开始时间
        startTimeThreadLocal.set(beginTime);//线程绑定变量（该数据只有当前请求的线程可见）
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            String jwt = request.getHeader(TokenProvider.AUTHORIZATION_HEADER);
            //登录认证
            if (StringUtils.hasText(jwt) ) {
                //校验jwt是否合法
                if (!jwt.startsWith("Bearer_offline")) {
                    //格式不对
                    responseMessage(response, AjaxResult.error(ResultMessageConstants.B00011.message()));
                    return false;
                }
                String  jwt_check= jwt.substring(14, jwt.length());
                /**
                 * 这里是需要更换token的异常情况
                 */
                if(!this.tokenProvider.validateToken(jwt_check)){
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code","0");
                    jsonObject.put("message","请刷新token!");
                    responseMessage(response, jsonObject);
                    return false;
                }
                User cache_User = redisCache.getCache(jwt, User.class);
                Integer userType = cache_User.getUserType();
                if(userType!=OfflineConstants.offlineRole.system){
                    //如果不是管理员，那么必须控制登录在线时间
                    Date startDate = cache_User.getStartDate();
                    Date endDate = cache_User.getEndDate();
                    if(startDate==null||endDate==null){
                        logger.info("非管理员角色，请申请在线时间!");
                        responseMessage(response, AjaxResult.error(ResultMessageConstants.B00012.message()));
                        return  false;

                    }else{
                        Date date=new Date();
                        if(!date.after(startDate)||!date.before(endDate)){
                            logger.info("非管理员角色，禁止在非指定时间内使用系统!");
                            responseMessage(response, AjaxResult.error(ResultMessageConstants.B00013.message()));
                            return  false;
                        }
                    }

                }
                userContext = new UserContext(cache_User);
                //权限认证
                Authentication auth = ((HandlerMethod) handler).getMethodAnnotation(Authentication.class);
                if(auth==null){
                    return true;
                }
                if (!Constant.SYSTEM_SUPER_USER.equals(cache_User.getId())) {
                    String menuUrl = auth.menu();
                    String permission_str = redisCache.getCache(Constant.REDIS_PERMISSION_CACHE_KEY+ jwt, String.class);
                    Set permission = JSONObject.parseObject(permission_str, Set.class);
                    if (permission.contains(menuUrl)) {
                        return true;
                    }
                    logger.info("没有权限!");
                    responseMessage(response, AjaxResult.error(ResultMessageConstants.B00006.message()));
                    return false;
                } else {
                    return true;
                }
            } else {
                logger.info("认证未通过,未登陆异常!");
                responseMessage(response, AjaxResult.error(ResultMessageConstants.B00003.message()));
                return false;
            }
        } else {
            logger.info("认证未通过,未知的接口!");
            responseMessage(response, AjaxResult.error(ResultMessageConstants.B00011.message()));
            return false;
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (userContext != null) {
            userContext.close();
        }
        long endTime = System.currentTimeMillis();//2、结束时间
        long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）
        long consumeTime = endTime - beginTime;//3、消耗的时间
        if (consumeTime > 2000) {//此处认为处理时间超过500毫秒的请求为慢请求
            //TODO 记录到日志
            logger.info(String.format("%s consume %d millis", request.getRequestURI(), consumeTime));
        }
        super.afterCompletion(request, response, handler, ex);
    }


    private boolean isNotValidatePermission(HttpServletRequest request, String authorization) {
        String key = request.getHeader(Constant.EIPSERVICE_KEY_NAMME);
        return Constant.EIPSERVICE_KEY_VALUE.equalsIgnoreCase(key);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TokenProvider.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer_offline")) {
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }

}
