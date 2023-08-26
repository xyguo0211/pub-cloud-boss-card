package com.cn.school.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**|
 * 创蓝短信接入
 */
@Configuration
@RefreshScope
public class SmsSendServiceUtil {
    Logger log= LoggerFactory.getLogger("SmsSendServiceUtilLog");

    public static final String charset = "utf-8";
    // API账号(验证码通知短信)
    @Value("${chuannan.phone.loginname}")
    private String account ;
    // API密码(验证码通知短信)
    @Value("${chuannan.phone.password}")
    private String password ;
    @Value("${chuannan.phone.url}")
    private String smsSingleRequestServerUrl ;

    /**
     *
     * 发送验证码通知短信
     * @param msg 设置您要发送的内容：其中“【】”中括号为运营商签名符号，多签名内容前置添加提交
     * @param phone 手机号码
     * @return
     */
    public RpcBaseResponseResult sendMassage(String msg, String phone) {
        RpcBaseResponseResult result = new RpcBaseResponseResult();

        //短信发送的URL 请登录zz.253.com 获取完整的URL接口信息
        //String smsSingleRequestServerUrl = "https://smssh1.253.com/msg/send/encrypt/json";
        //String smsSingleRequestServerUrl = "https://smssh1.253.com/msg/send/json";
        //状态报告
        String report= "true";

        try {
            SmsSendRequest smsSingleRequest = new SmsSendRequest(account, password, msg, phone,report);

            String requestJson = JSON.toJSONString(smsSingleRequest);

            log.info("before request string is: " + requestJson);

            String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);

            log.info("response after request result is :" + response);

            SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);

            log.info("response  toString is :" + smsSingleResponse);

            result.setStatus(Integer.parseInt(smsSingleResponse.getCode())); // 状态码
            result.setMessage(smsSingleResponse.getErrorMsg()); // 状态码说明（成功返回空）
            result.setData(smsSingleRequest.getMsg());
        } catch (Exception e){
            e.printStackTrace();
            result.setStatus(RpcBaseResponseResult.STATUS_ERROR);
            result.setMessage("系统错误...");
        }
        return result;
    }

}
