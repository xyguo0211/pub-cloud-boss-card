package com.cn.auth.config;

import com.alibaba.fastjson.JSONObject;
import com.cn.auth.config.jwt.TokenProvider;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class AuthorityInterceptor extends HandlerInterceptorAdapter {

    protected Logger logger = LoggerFactory.getLogger(AuthorityInterceptor.class);
    private TokenProvider tokenProvider;
    private UserContext userContext;
    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("StopWatch-StartTime");

    @Autowired
    private RedisCache redisCache;

    public AuthorityInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
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
            String referer = request.getHeader("referer");
            if (null != referer && referer.toLowerCase().contains("swagger-ui".toLowerCase())) {
                request.setAttribute("MANAGE_REQUEST_DEPART_CODE", "1");
                request.setAttribute("MANAGE_REQUEST_USER_ID", 1);
                return true;
            }

            String jwt = resolveToken(request);
            User user = null;
            String userId = null;
            boolean isValidatePermission = true;//是否需要验证权限
            //登录认证
            if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
                userId = this.tokenProvider.getUserId(jwt);
                user = redisCache.getCache(
                        Constant.REDIS_USER_CACHE_KEY+
                        Constant.REDIS_USER_CACHE_ID_ + userId, User.class
                );
                if (null == user) {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code","0");
                    jsonObject.put("message","未登录用户，请先登录");
                    responseMessage(response, jsonObject);
                    return false;
                }
                if (user.getStatus() == Constant.MUSER_STATUS_NO) {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code","0");
                    jsonObject.put("message","用户已被禁用,请联系管理员!");
                    responseMessage(response, jsonObject);
                    redisCache.del(Constant.REDIS_USER_CACHE_KEY+Constant.REDIS_USER_CACHE_ID_ + userId);
                    return false;
                }

                userContext = new UserContext(user);

               /* request.setAttribute(Constant.MANAGE_REQUEST_DEPART_CODE, user.getDepartCode());*/
                request.setAttribute(Constant.MANAGE_REQUEST_USER_ID, user.getId());

            } else {
                // token有问题
                if (isNotValidatePermission(request, jwt)) {
                    return true;
                }
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("code","0");
                jsonObject.put("message","认证未通过,token异常!");
                responseMessage(response, jsonObject);
                return false;
            }
            //权限认证

            Authentication auth = ((HandlerMethod) handler).getMethodAnnotation(Authentication.class);
            // 没有声明需要权限,或者声明不验证权限 或者是服务端接口调用也不验证权限
            if (auth == null || !auth.validate() || isNotValidatePermission(request, jwt))
                return true;
            else {
                if (!Constant.SYSTEM_SUPER_USER.equals(user.getId())) {
                    AuthorityType[] auths = auth.type();
                    if (auths.length == 1 && auths[0].equals(AuthorityType.NON)) {
                        return true;
                    }
                    String menuUrl = auth.menu();
                    request.setAttribute(Constant.MANAGE_REQ_MENUURL, menuUrl);
                    Map<String, String> permission = redisCache.getCache(
                            Constant.REDIS_PERMISSION_CACHE_KEY+
                            Constant.REDIS_PERMISSION_CACHE_ID_ + userId,
                            Map.class
                    );
                    String jurisd = permission.get(menuUrl);
                    if (jurisd != null) {
                        List<String> jurisdList = Arrays.asList(jurisd.split(","));
                        for (AuthorityType at : auths) {
                             if (jurisdList.contains(String.valueOf(at.getLevel())))
                                return true;
                        }
                    }
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("code","0");
                    jsonObject.put("message","没有权限!");
                    responseMessage(response, jsonObject);
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return true;
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
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        String pathToken = request.getParameter(TokenProvider.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(pathToken) && pathToken.startsWith("Bearer ")) {
            return pathToken.substring(7, pathToken.length());
        }
        return null;
    }

}
