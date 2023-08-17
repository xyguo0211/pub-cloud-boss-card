package com.cn.school.util;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Log4j2
public class WeixinApiClient {


    private static int timeout = 5;

    /**
     * @param paramsStr
     * @return
     */
    public static String weixinPay(String urlStr,String paramsStr) {
        log.info("[weixinPay],url:{}===[generatorCost],paramsStr:{}",urlStr,paramsStr);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, paramsStr);
        Request request = new Request.Builder().url(urlStr).get().post(requestBody).addHeader("Accept", "*/*")
        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
        .addHeader("Host", "api.mch.weixin.qq.com")
        .addHeader("X-Requested-With", "XMLHttpRequest")
        .addHeader("Cache-Control", "max-age=0")
        .addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ")
        .build();
        try {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false) //不自动重连
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            String responseBodyStr = response.body().string();
            if (StringUtils.isBlank(responseBodyStr)){
                log.info("请求接口:{},响应报文为空！", urlStr);
                throw new RemoteAccessException("请求接口:" + urlStr + ",响应报文为空！");
            }
            log.info("[weixinPay],responseBodyStr:{}",responseBodyStr);
            return responseBodyStr;
        } catch (IOException e) {
            log.error("获取请求返回异常，{}", e.getMessage());
        }
        return null;
    }

}
