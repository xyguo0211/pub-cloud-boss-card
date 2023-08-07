package com.cn.auth.security;

import com.cn.auth.config.AuthorityInterceptor;
import com.cn.auth.config.OnlineAuthorityInterceptor;
import com.cn.auth.config.jwt.TokenProvider;
import com.pub.redis.service.RedisService;
import com.pub.redis.util.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ganyongheng on 2017/6/14 0014.
 */
@Configuration
@EnableWebMvc
public class ServletInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private RedisCache redisCache;

    private final Logger log = LoggerFactory.getLogger(ServletInterceptorConfig.class);

    private  TokenProvider tokenProvider;

    public ServletInterceptorConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /** 不需要拦截地址 */
    public static final String[] excludeUrls_offline = {"/offline/userDo/login", "/userDo/logout", "/offline/userDo/refreshToken" };
    public static final String[] excludeUrls_online = {"/online/userDo/login", "/userDo/logout", "/online/userDo/refreshToken" , "/online/userDo/register","/online/userDo/sendEmail","/online/sysDataDictionaryDo/refreshCache"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 离线的拦截器
         */
        registry.addInterceptor(new AuthorityInterceptor(tokenProvider,redisCache))
                .excludePathPatterns(excludeUrls_offline)
                .addPathPatterns("/offline/ganyongheng/**");

        /**
         * 在线的拦截器
         */
        registry.addInterceptor(new OnlineAuthorityInterceptor(tokenProvider,redisCache))
                .excludePathPatterns(excludeUrls_online)
                .addPathPatterns("/online/**");



    }




    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return factory;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
