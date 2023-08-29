package com.cn.school.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.cn.school.config.BaseWxConfig;
import com.cn.school.config.Constant;
import com.cn.school.config.WxPayConfig;
import com.cn.school.entity.TripCarDo;
import com.cn.school.entity.TripOrderDo;
import com.cn.school.entity.TripProductDo;
import com.cn.school.entity.UserDo;
import com.cn.school.entity.dto.UnlimitedQRCodeParam;
import com.cn.school.mapper.TripOrderMapper;
import com.cn.school.service.ITripOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.school.util.WXPayUtil;
import com.cn.school.util.WeixinApiClient;
import com.cn.school.util.WxPayRequstUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pub.core.exception.BusinessException;
import com.pub.redis.util.RedisCache;
import com.sun.org.apache.regexp.internal.RE;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.cn.school.util.WXPayConstants.SignType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private com.cn.school.config.BaseWxConfig baseWxConfig;


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
    @Transactional
    public  void addTripOrder(TripOrderDo tripOrderDo) throws Exception{
        save(tripOrderDo);
        Integer num = tripOrderDo.getNum();
        tripCarServiceImpl.addTicket(tripOrderDo.getCarId(),num,Constant.TicketAddStatus.DEL);
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

    /**
     * 生成退款订单号
     * @return
     */
    public  String createRefundsTaskNo() {
        String yyyyMMddStr = DateUtils.formatDate(new Date(), "yyyyMMdd");
        //设置两天过期  1000*60*60*48
        String hincr = redisCache.hincr(yyyyMMddStr, yyyyMMddStr, 1, 1000*60*60*48)+"";
        if(hincr.contains(".")){
            String[] split = hincr.split("\\.");
            String rtn = String.format("%08d", Integer.valueOf(split[0]));
            return "RF"+yyyyMMddStr+rtn;
        }else{
            String rtn = String.format("%08d", Integer.valueOf(hincr));
            return "RF"+yyyyMMddStr+rtn;
        }
    }


    public String tets(TripOrderDo tripOrderDo,String openid) throws Exception {
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type","application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid",wxPayConfig.getMchid())
                .put("appid", wxPayConfig.getAppid())
                .put("description", "校园车租车小程序预支付申请")
                .put("notify_url", wxPayConfig.getNotifyUrl())
                .put("out_trade_no", tripOrderDo.getOrderId());
        rootNode.putObject("amount")
                .put("total",Integer.valueOf(tripOrderDo.getTotalFee()));
        rootNode.putObject("payer")
                .put("openid", openid);

        objectMapper.writeValue(bos, rootNode);
        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        /**
         *
         merchantId商户号。
         merchantSerialNumber商户API证书的证书序列号。
         merchantPrivateKey商户API私钥，如何加载商户API私钥请看常见问题。
         wechatPayCertificates微信支付平台证书列表。你也可以使用后面章节提到的“定时更新平台证书功能”，而不需要关心平台证书的来龙去脉。

         */
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(Constant.privateKey);
        X509Certificate wechatPayCertificate = PemUtil.loadCertificate(
                new ByteArrayInputStream(Constant.certificate.getBytes(StandardCharsets.UTF_8)));

        ArrayList<X509Certificate> listCertificates = new ArrayList<>();
        listCertificates.add(wechatPayCertificate);

        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(wxPayConfig.getMchid(), wxPayConfig.getBusinessPayId(), merchantPrivateKey)
                .withWechatPay(listCertificates);

        CloseableHttpClient httpClient = builder.build();
        CloseableHttpResponse response = httpClient.execute(httpPost);

        String prepay_id = EntityUtils.toString(response.getEntity());

        return prepay_id;
    }

    public static RSAAutoCertificateConfig config = null ;
    String filePath ="C:/Users/Administrator/Desktop/card/cert/apiclient_key.pem";//测试环境可放到resource目录下
    public static JsapiServiceExtension service = null ;
    public PrepayWithRequestPaymentResponse WeChartPay(TripOrderDo tripOrderDo,String openid) {
        //元转换为分
        Integer amountInteger = Integer.valueOf(WxPayRequstUtil.getMoney(tripOrderDo.getTotalFee()));
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        if (config == null) {
            synchronized ("createConfig"){
                if (config == null) {
                    config = new RSAAutoCertificateConfig.Builder()
                            .merchantId(wxPayConfig.getMchid())
                            .privateKey(Constant.privateKey)
                            .merchantSerialNumber(wxPayConfig.getBusinessPayId())
                            //.apiV3Key("llikjdkYY2546525llYkjdk2546525ll")
                            .apiV3Key(wxPayConfig.getApiV3Key())
                            .build();
                    // 构建service
                    if (service == null) {
                        service = new JsapiServiceExtension.Builder().config(config).build();
                    }
                }

            }

        }
        //组装预约支付的实体
        PrepayRequest request = new PrepayRequest();
        //计算金额
        Amount amount = new Amount();
        amount.setTotal(amountInteger);
        amount.setCurrency("CNY");
        request.setAmount(amount);
        //公众号appId
        request.setAppid(wxPayConfig.getAppid());
        //商户号
        request.setMchid(wxPayConfig.getMchid());
        //支付者信息
        Payer payer = new Payer();
        payer.setOpenid(openid);
        request.setPayer(payer);
        //描述
        request.setDescription(wxPayConfig.getDescription());
        //微信回调地址，需要是https://开头的，必须外网可以正常访问
        //本地测试可以使用内网穿透工具，网上很多的
        request.setNotifyUrl(wxPayConfig.getNotifyUrl());
        //订单号
        request.setOutTradeNo(tripOrderDo.getOrderId());
        // 加密
        PrepayWithRequestPaymentResponse payment = service.prepayWithRequestPayment(request);
        //默认加密类型为RSA
        payment.setSignType("MD5");
        //返回数据，前端调起支付
        return payment;
    }

    public void createTripOrder(TripOrderDo tripOrderDo) throws Exception{
        TripCarDo tripCarDo = tripCarServiceImpl.getById(tripOrderDo.getCarId());
        Integer orderNum = tripCarDo.getOrderNum();
        Integer sellNum = tripCarDo.getSellNum();
        if( orderNum-sellNum-tripOrderDo.getNum()<0){
            //说明没票了
            throw  new BusinessException("余票不足！请刷新购买页面");
        }
        User currentUser = UserContext.getCurrentUser();
        Integer user_id = currentUser.getId();
        UserDo userDo = userServiceImpl.getById(user_id);
        String taskNo = createTaskNo();
        tripOrderDo.setStarTime(tripCarDo.getStartTime());
        tripOrderDo.setEndTime(tripCarDo.getEndTime());
        tripOrderDo.setUserId(userDo.getId());
        tripOrderDo.setPhone(tripOrderDo.getPhone());
        tripOrderDo.setIdentityName(tripOrderDo.getIdentityName());
        tripOrderDo.setOrderId(taskNo);
        /**
         * 初始化
         */
        tripOrderDo.setStatus(0);
        /**
         * 未上车
         */
        tripOrderDo.setOnCarStatus(0);
        /**
         * 未退票
         */
        tripOrderDo.setTicketStatus(-1);
        tripOrderDo.setCreateTime(new Date());
    }
    @Transactional
    public void wxPayNotify(TripOrderDo tripOrderDo) throws Exception{
        // 订单状态   0 初始  1成功  -1 失败
        tripOrderDo.setPayTime(new Date());
        Integer status = tripOrderDo.getStatus();
        if(1==status){
            /**
             * 生成二维码
             */

        }else{
            /**
             * 释放车票
             */
            Integer ticketStatus = tripOrderDo.getTicketStatus();
            if(ticketStatus!=9){
                tripOrderDo.setTicketStatus(9);
                Integer num = tripOrderDo.getNum();
                Integer carId = tripOrderDo.getCarId();
                tripCarServiceImpl.addTicket(carId,num,Constant.TicketAddStatus.ADD);
            }
        }
        updateById(tripOrderDo);
    }


    public String getAccessToken(){
        String access_token_school = redisCache.getStringCache("access_token_school");
        if(StringUtils.isNotBlank(access_token_school)){
            return access_token_school;
        }else{
            //1.通过code获取access_token
            String url = "https://api.weixin.qq.com/cgi-bin/token?appid=APPID&secret=SECRET&grant_type=client_credential";
            url = url.replace("APPID", wxPayConfig.getAppid()).replace("SECRET", wxPayConfig.getWxAppSecret());
            ResponseEntity<String> tokenData = restTemplate.getForEntity(url, String.class);
            String tokenInfoStr = tokenData.getBody();
            if(StringUtils.isNotBlank(tokenInfoStr)){
                JSONObject tokenInfoObject = JSONObject.parseObject(tokenInfoStr);
                log.info("getAccessToken:{}", tokenInfoObject);
                String access_token = tokenInfoObject.getString("access_token");
                Long expires_in = tokenInfoObject.getLong("expires_in");
                if(StringUtils.isNotBlank(access_token)){
                    //提前十分钟过期
                    redisCache.putCacheWithExpireTime("access_token_school",access_token,expires_in-600);
                    return access_token;
                }
            }
            return null;

        }



    }
    public byte[] getImageByte(UnlimitedQRCodeParam body) throws Exception{
        String accessToken = getAccessToken();
        if(StringUtils.isBlank(accessToken)){
            throw new BusinessException("获取token异常！");
        }
        //1.通过code获取access_token
        ResponseEntity<byte[]> responseEntity = restTemplate.postForEntity(wxPayConfig.getUrlToken() + "?access_token=" + accessToken, body, byte[].class);
        byte[] urls = responseEntity.getBody();
        return urls;
    }

    /**
     * 单机部署，不存在分布式，加个锁就行了
     * @param orderId
     * @param carId
     * @throws Exception
     */
    public synchronized TripOrderDo checkTripOrder(Integer orderId, Integer carId) throws Exception{
        TripOrderDo tripOrderDo = getById(orderId);
        Integer onCarStatus = tripOrderDo.getOnCarStatus();
        /**
         * 0 未上车  1 已上车  -1 已过期
         */
        if(onCarStatus!=0){
            throw new BusinessException("该车票已被核销过！");
        }
        Integer carIdDb = tripOrderDo.getCarId();
        if(carIdDb!=carId){
            throw new BusinessException("车次不正确！");
        }
        tripOrderDo.setOnCarStatus(1);
        tripOrderDo.setOncarTime(new Date());
        updateById(tripOrderDo);
        return tripOrderDo;
    }

    public void refundsTripOrder(Integer orderId) throws Exception {
        TripOrderDo tripOrderDo = getById(orderId);
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type","application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("out_trade_no",tripOrderDo.getOrderId());
        /**
         * 退款订单号
         */
        String refundsTaskNo = createRefundsTaskNo();
        tripOrderDo.setRefundOrderId(refundsTaskNo);
        rootNode.put("out_refund_no", refundsTaskNo);
        rootNode.putObject("amount")
                /**
                 * 退款金额
                 */
                .put("refund",1)
                /**
                 * 原订单金额
                 */
                .put("total", 1)
                .put("currency", "CNY");

        objectMapper.writeValue(bos, rootNode);
        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        /**
         *
         merchantId商户号。
         merchantSerialNumber商户API证书的证书序列号。
         merchantPrivateKey商户API私钥，如何加载商户API私钥请看常见问题。
         wechatPayCertificates微信支付平台证书列表。你也可以使用后面章节提到的“定时更新平台证书功能”，而不需要关心平台证书的来龙去脉。

         */
        CloseableHttpClient httpClient = baseWxConfig.getHttpClient();
        CloseableHttpResponse response = httpClient.execute(httpPost);

        String prepay_id = EntityUtils.toString(response.getEntity());

    }


}
