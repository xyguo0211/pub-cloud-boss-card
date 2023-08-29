package com.cn.school.config;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

@Component
public class BaseWxConfig {

    @Autowired
    private WxPayConfig wxPayConfig;

    CloseableHttpClient httpClient;

    public CloseableHttpClient getHttpClient(){
        if(httpClient==null){
            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(Constant.privateKey);
            X509Certificate wechatPayCertificate = PemUtil.loadCertificate(
                    new ByteArrayInputStream(Constant.certificate.getBytes(StandardCharsets.UTF_8)));

            ArrayList<X509Certificate> listCertificates = new ArrayList<>();
            listCertificates.add(wechatPayCertificate);

            WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                    .withMerchant(wxPayConfig.getMchid(), wxPayConfig.getBusinessPayId(), merchantPrivateKey)
                    .withWechatPay(listCertificates);

            httpClient = builder.build();
        }
        return httpClient;

    }
}
