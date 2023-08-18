package com.cn.school.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.cn.school.config.WxPayConfig;
import com.cn.school.entity.TripCarDo;
import com.cn.school.entity.TripOrderDo;
import com.cn.school.entity.TripProductDo;
import com.cn.school.entity.UserDo;
import com.cn.school.mapper.TripOrderMapper;
import com.cn.school.service.ITripOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.school.util.WXPayUtil;
import com.cn.school.util.WeixinApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pub.core.exception.BusinessException;
import com.pub.redis.util.RedisCache;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.cn.school.util.WXPayConstants.SignType;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-16
 */
@Log4j2
@Service
public class TripOrderServiceImpl extends ServiceImpl<TripOrderMapper, TripOrderDo> implements ITripOrderService {

    @Autowired
    private TripProductServiceImpl tripProductService;
    @Autowired
    private TripCarServiceImpl tripCarServiceImpl;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private WxPayConfig wxPayConfig;

    private int timeout = 30;

    public List<TripOrderDo> myTripOrderDo(Integer status) {
        User currentUser = UserContext.getCurrentUser();
        Integer id = currentUser.getId();
        QueryWrapper<TripOrderDo> wq=new QueryWrapper<>();
        if(status!=null){
            //0 未发车  1 已发车
            if(status==1){
                wq.le("star_time", new Date());
            }else{
                wq.ge("star_time", new Date());
            }
        }
         wq.eq("user_id", id);
        List<TripOrderDo> list = list(wq);
        return list;
    }

    /**
     * 这里一定要加上锁,此时一定要锁单
     * @param tripOrderDo
     */
    public synchronized void addTripOrder(TripOrderDo tripOrderDo) throws Exception{
        TripCarDo tripCarDo = tripCarServiceImpl.getById(tripOrderDo.getCarId());
        Integer orderNum = tripCarDo.getOrderNum();
        Integer sellNum = tripCarDo.getSellNum();
        if( orderNum-sellNum-tripOrderDo.getNum()<0){
            //说明没票了
            throw  new BusinessException("余票不足！请刷新购买页面");
        }
        User currentUser = UserContext.getCurrentUser();
        String taskNo = createTaskNo();
        tripOrderDo.setStarTime(tripCarDo.getStartTime());
        tripOrderDo.setEndTime(tripCarDo.getEndTime());
        //tripOrderDo.setUserId(currentUser.getId());
        tripOrderDo.setUserId(1);
        tripOrderDo.setOrderId(taskNo);
        tripOrderDo.setStatus(0);
        tripOrderDo.setOnCarStatus(0);
        tripOrderDo.setCreateTime(new Date());
        save(tripOrderDo);
        Integer num = tripOrderDo.getNum();
        tripCarDo.setSellNum(sellNum+num);
        tripCarServiceImpl.updateById(tripCarDo);
    }

    /**
     * 拉起微信支付
     * @param tripOrderDo
     */
    public void submitTripOrder(TripOrderDo tripOrderDo){
        Integer userId = tripOrderDo.getUserId();
        UserDo userDo = userServiceImpl.getById(userId);
        String openid = userDo.getOpenid();
            String url = wxPayConfig.getUrl();
            String reqStr = getReqStr(openid,tripOrderDo); //组装预下单的请求数据
            String results = WeixinApiClient.weixinPay(url,reqStr);//发送post数据到微信预下单
            Map<String,String> return_data = null;
            try {
                return_data = WXPayUtil.xmlToMap(results);//微信的一个工具类
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
            String return_code = return_data.get("return_code");
            log.info("return_code=" + return_code);
            if("SUCCESS".equals(return_code)){
                String prepay_id = return_data.get("prepay_id");
            }else{
                results ="{\"return_code\":\"fail\"}";
            }

    }



    public  String getReqStr(String openid,TripOrderDo tripOrderDo){
        JSONObject data = new JSONObject();
        data.put("appid", wxPayConfig.getAppid());
        data.put("mchid",wxPayConfig.getMchid());
        data.put("out_trade_no", tripOrderDo.getOrderId());
        data.put("notify_url",wxPayConfig.getNotifyUrl());
        data.put("description","校园旅行大巴服务");
        //金额
        JSONObject amount = new JSONObject();
        amount.put("currency","CNY");
        amount.put("total",Integer.valueOf(tripOrderDo.getTotalFee()));
        data.put("amount",amount);
        //支付者
        JSONObject payer = new JSONObject();
        payer.put("openid",openid);
        data.put("payer",payer);
        data.put("sign_type", "MD5");
        try {
            String sign = WXPayUtil.generateSignature(null, "merKey", SignType.MD5);
            data.put("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("sign error");
        }
        String reqBody = null;
        try {
            reqBody = WXPayUtil.mapToXml(null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return reqBody;
    }

    public  String createTaskNo() {
        String yyyyMMddStr = DateUtils.formatDate(new Date(), "yyyyMMdd");
        //设置两天过期  1000*60*60*48
        String hincr = redisCache.hincr(yyyyMMddStr, yyyyMMddStr, 1, 1000*60*60*48)+"";
        if(hincr.contains(".")){
            String[] split = hincr.split("\\.");
            String rtn = String.format("%08d", Integer.valueOf(split[0]));
            return "JF"+yyyyMMddStr+rtn;
        }else{
            String rtn = String.format("%08d", Integer.valueOf(hincr));
            return "JF"+yyyyMMddStr+rtn;
        }
    }
    private static final String privateKey ="-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDTaSFJTqrbIEPE\n" +
            "AynS5/I7+2ZTSoHkNX9IT6bBXMBrJuZATUHRNdgOw+KqffgHU3nuUBCU7kWQRIK6\n" +
            "LAoy7AsDYgtqmaB0ZyhRua0PspADLyz57bVizLiNBBLeB7fENcksdcYeNiQg2C89\n" +
            "AxWMSHy+UF+FNo2b3qU3ePurvybAinSaT1B+Bl9sVcq75EQJuCBBj3V/z70ZAv1j\n" +
            "DQxDX/Xlnwlyrw1jkladLmzImWSv/sNkE+YLYf6VSpWfcPeNuqfm6wJ+V97f76mu\n" +
            "L8mdSNo2v/57UgkUh/KaCvvwCQxbYrj3wjCMbJTZi+t8qGksdArU1MmoJX0WwKST\n" +
            "XEuaWd/1AgMBAAECggEBAIlf9StHnSqKyr4iOBk+c1+auyFAdystwCni6D8Z4EdA\n" +
            "nboG+c/SpzThAPc8p+FK0x6SlFPSiQ14F2KWn4H7dCScn0KD1YoORlrkxpo+s+n9\n" +
            "y8IUPxuWYA3yKbhxV25+bN0hIr4a/FsDX57L1EK2D6kzXP6ZNmekw8NKMG+n5KyT\n" +
            "nS//s4BcdKSb1sLNZx75lkhhE0gmbhjW9UtLJvLdK2jPEU68Vu97wn4kbES6yoTF\n" +
            "mjidECulCsWUj8TfjDR6S9sG2Gj25tZjZcWwcxO4v1N6vELH+v9tAsFmCphu0Aul\n" +
            "SsjNCeaynbSxGZgVBNiW+wOQbeXhc41iu1ALpFnvdXkCgYEA8MJsxVXDRXJKhkSh\n" +
            "OcwAVe+bEWPplcg6fydfZUEEY3ERR4z6a91JHz+71V1fZu6zs1mjHdT0n0Cj/YOl\n" +
            "tlx2wh9JNpGIjOW0CxAMPmd+h8LU7sGcPAnUz+NF7WDQDQDksPYw9kRGDiBZ8qJ4\n" +
            "VutG+8DbX1sgT0rdgymQAvUHOfsCgYEA4MsZdFNjTeiViFq4C7GNfJ4P+SW8QLxq\n" +
            "lS7Y7sjpkiekNxWClRHwF4eIoG3RK2hxyz7T8NtxpmSuWk2YoepHrsem36Gl2uJz\n" +
            "VPvnHOmdh/bxCc0W37FPichINn8B7jTRKnZ0DmmYOQlk3NH4+kJzkxiqnxpTBHgi\n" +
            "mzkQagkTms8CgYAdPvDpk75xvC1zW/jdxXsw9Tc4CJQCXt1EPusmqJw43C5GK8jr\n" +
            "u2i7hAl0JLCHF2361mOrJwhEJB0HmatSmK7Qa+5/03Pr8adKRLvIBNho83DcQ+aP\n" +
            "oH4adrgy2rTLL5WYLX/LGoYMB0AF6liF7nSj9kxvq+kj2KtJ2I2m3k7vawKBgQC/\n" +
            "/fdhmQ8JrYp5iTIEGsODGeT+oLImgEZv4DE70LFdOSpSObbr5wQutH2GuASclHoM\n" +
            "Yz7VSjfJK9iWHAwuzlAnATKPchqb1ik2/mcoFIeNZuX7vwS4TVJnlX3HvbZCYy36\n" +
            "nG0HGjz/CfzxdQy3giYADmM7vFoHSSwVcymHxvTNlQKBgQDwJ+lbSlGc1H7X5PZS\n" +
            "8mHxvo69vc+W0yLNN13Ln073Ly2TGtYrEQYEx6ZJhYxP1ZZuFgYefKdfSKCMRPXN\n" +
            "87DTpwywHrJes7WuuQXkYgO4RShkrB4i+pTsD4Pue6fQXyMtugAMkk5c1Tlya8/V\n" +
            "WIdne+MBmEReP18p43KXyX94xg==\n" +
            "-----END PRIVATE KEY-----\n";
    private static final String certificate  ="-----BEGIN CERTIFICATE-----\n" +
            "MIIEFDCCAvygAwIBAgIUJsYsihcApwmmPEIZG4gcpS/of14wDQYJKoZIhvcNAQEL\n" +
            "BQAwXjELMAkGA1UEBhMCQ04xEzARBgNVBAoTClRlbnBheS5jb20xHTAbBgNVBAsT\n" +
            "FFRlbnBheS5jb20gQ0EgQ2VudGVyMRswGQYDVQQDExJUZW5wYXkuY29tIFJvb3Qg\n" +
            "Q0EwHhcNMjMwODE3MDYzMDUzWhcNMjgwODE1MDYzMDUzWjBuMRgwFgYDVQQDDA9U\n" +
            "ZW5wYXkuY29tIHNpZ24xEzARBgNVBAoMClRlbnBheS5jb20xHTAbBgNVBAsMFFRl\n" +
            "bnBheS5jb20gQ0EgQ2VudGVyMQswCQYDVQQGDAJDTjERMA8GA1UEBwwIU2hlblpo\n" +
            "ZW4wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC7eO2e4e6Z2sKUJReu\n" +
            "674uarydzbRgfRC6Wv/l1ImZldXvM0pVQcfaBaBthmfc22qNQAHtX+6J3Dwq2IiI\n" +
            "9dYo267TGWJg9MAUeaBkZDy7dAnA8MTmZM168aGoLLTYQhT5FSbCpdmQvPAsM+Km\n" +
            "If4rGA6hwSc6ImOI9oFZ1H65KCjBYIJFhRyZuQg01pRuDERLq3eJXBLg56JxWMf1\n" +
            "NAA+6o5UQ+DqTk8Zg8c9p47JbPLp2XUxykXEgprlYfKmd5Ws/9ZlnkyubAZ0J/MN\n" +
            "I+x2jNvwi5z1gxT/NM6Wws3HPbauecw01lAGw+tPFdr+1r8RyKOYYKQwNpmoI4z+\n" +
            "bG7PAgMBAAGjgbkwgbYwCQYDVR0TBAIwADALBgNVHQ8EBAMCA/gwgZsGA1UdHwSB\n" +
            "kzCBkDCBjaCBiqCBh4aBhGh0dHA6Ly9ldmNhLml0cnVzLmNvbS5jbi9wdWJsaWMv\n" +
            "aXRydXNjcmw/Q0E9MUJENDIyMEU1MERCQzA0QjA2QUQzOTc1NDk4NDZDMDFDM0U4\n" +
            "RUJEMiZzZz1IQUNDNDcxQjY1NDIyRTEyQjI3QTlEMzNBODdBRDFDREY1OTI2RTE0\n" +
            "MDM3MTANBgkqhkiG9w0BAQsFAAOCAQEAL0YkFmjjr0Hwfut3kTqwywOxO+s778YA\n" +
            "S/GY2fsD0kKo2NCUxQ2stUnt/EuIlMYJnksgOAl3REdLuHDuNtXQunbl9zb9HenA\n" +
            "S1cmDXvdxPzoWZ3cWPMICDfYkeGUefaWY1gyIj85FWrZjrIxE6N9AQY8SjbZDWM5\n" +
            "3uHGPHZ28B2/DeexoqYGMa9lt3167CbOBS7cmbSOKmyD99/o69BOjWAX1GVQmMY2\n" +
            "ATMTsmtdYRpxRJ1uzZ/cqqnDeaduJ/1icc4OagD4WEhbrIJpx707wKJRG2VXkHXj\n" +
            "ER3loKnO3uOc2tMq9cjYVihX26EjxLkqMRByE/X0j5hVfR8uafBMuw==\n" +
            "-----END CERTIFICATE-----";

    public void tets() throws Exception {
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type","application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid","1650712342")
                .put("appid", "wx81e195d01798b996")
                .put("description", "Image形象店-深圳腾大-QQ公仔")
                .put("notify_url", "http://xhx.ygb56.com:60235/online_use/online/wx/wxCallback")
                .put("out_trade_no", "1217752501201407033233368018");
        rootNode.putObject("amount")
                .put("total", 1);
        rootNode.putObject("payer")
                .put("openid", "wx81e195d01798b996");

        objectMapper.writeValue(bos, rootNode);

        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        /**
         *
         merchantId商户号。
         merchantSerialNumber商户API证书的证书序列号。
         merchantPrivateKey商户API私钥，如何加载商户API私钥请看常见问题。
         wechatPayCertificates微信支付平台证书列表。你也可以使用后面章节提到的“定时更新平台证书功能”，而不需要关心平台证书的来龙去脉。

         */
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(privateKey);
        X509Certificate wechatPayCertificate = PemUtil.loadCertificate(
                new ByteArrayInputStream(certificate.getBytes(StandardCharsets.UTF_8)));

        ArrayList<X509Certificate> listCertificates = new ArrayList<>();
        listCertificates.add(wechatPayCertificate);

        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant("1650712342", "755C5E96EAA985960035B577C12AFBFD1669C48E", merchantPrivateKey)
                .withWechatPay(listCertificates);

        CloseableHttpClient httpClient = builder.build();
        CloseableHttpResponse response = httpClient.execute(httpPost);

        String bodyAsString = EntityUtils.toString(response.getEntity());
        System.out.println(bodyAsString);
    }
    // 你的商户私钥
    private static final String merchantId = "1650712342"; // 商户号（服务商）
    private static final String merchantSerialNumber = "755C5E96EAA985960035B577C12AFBFD1669C48E"; // 商户证书序列号
    private static final String apiV3Key = "llikjdkYY2546525llYkjdk2546525ll"; // API V3密钥
    private CloseableHttpClient httpClient;
    CertificatesManager certificatesManager;
    Verifier verifier;

    private static final HttpHost proxy = null;

    public void test02() throws Exception {
       /* PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(privateKey);*/
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(
                new FileInputStream("C:\\Users\\Administrator\\Desktop\\card\\cert\\apiclient_key.pem"));
        // 获取证书管理器实例
        certificatesManager = CertificatesManager.getInstance();
        // 添加代理服务器
        certificatesManager.setProxy(proxy);
        // 向证书管理器增加需要自动更新平台证书的商户信息
        certificatesManager.putMerchant(merchantId, new WechatPay2Credentials(merchantId,
                        new PrivateKeySigner(merchantSerialNumber, merchantPrivateKey)),
                apiV3Key.getBytes(StandardCharsets.UTF_8));
        // 从证书管理器中获取verifier
        verifier = certificatesManager.getVerifier(merchantId);
        // 构造httpclient
        httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(merchantId, merchantSerialNumber, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier))
                .build();


        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type","application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid","1650712342")
                .put("appid", "wx81e195d01798b996")
                .put("description", "Image形象店-深圳腾大-QQ公仔")
                .put("notify_url", "http://xhx.ygb56.com:60235/online_use/online/wx/wxCallback")
                .put("out_trade_no", "1217752501201407033233368018");
        rootNode.putObject("amount")
                .put("total", 1);
        rootNode.putObject("payer")
                .put("openid", "wx81e195d01798b996");

        objectMapper.writeValue(bos, rootNode);

        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost);

        String bodyAsString = EntityUtils.toString(response.getEntity());
        System.out.println(bodyAsString);
    }

}
