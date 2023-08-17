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
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    public void tets() throws Exception {
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type","application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid","1900009191")
                .put("appid", "wxd678efh567hg6787")
                .put("description", "Image形象店-深圳腾大-QQ公仔")
                .put("notify_url", "https://www.weixin.qq.com/wxpay/pay.php")
                .put("out_trade_no", "1217752501201407033233368018");
        rootNode.putObject("amount")
                .put("total", 1);
        rootNode.putObject("payer")
                .put("openid", "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");

        objectMapper.writeValue(bos, rootNode);

        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        /**
         *
         merchantId商户号。
         merchantSerialNumber商户API证书的证书序列号。
         merchantPrivateKey商户API私钥，如何加载商户API私钥请看常见问题。
         wechatPayCertificates微信支付平台证书列表。你也可以使用后面章节提到的“定时更新平台证书功能”，而不需要关心平台证书的来龙去脉。

         */
        /*WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant("1650712342", merchantSerialNumber, merchantPrivateKey)
                .withWechatPay(wechatPayCertificates);
*/
        WechatPayHttpClientBuilder builder =null;
        CloseableHttpClient httpClient = builder.build();
        CloseableHttpResponse response = httpClient.execute(httpPost);

        String bodyAsString = EntityUtils.toString(response.getEntity());
        System.out.println(bodyAsString);
    }


}
