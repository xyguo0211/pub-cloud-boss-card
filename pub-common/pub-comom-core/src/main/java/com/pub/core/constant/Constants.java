package com.pub.core.constant;

/**
 * 通用常量信息
 * 
 * @author ruoyi
 */
public interface Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * 成功标记
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    public static final Integer FAIL = 500;

    /**
     * 登录成功状态
     */
    public static final String SUCCESS_STATUS = "0";

    /**
     * 登录失败状态
     */
    public static final String FAIL_STATUS = "-1";

    /**
     * 成功返回msg
     */
    public static final String MSG_SUCCESS = "Success";

    /**
     * 必要参数缺失返回码
     */
    public static final String Missing_Parameter="400";

    //系统错误
    public static final String Result_Code_Systemrror="999";


    public static final CharSequence HTTP = "http";
    public static final CharSequence HTTPS = "https";

    public static final String  llegal_Parameter="4001";
    interface PAY_PARAMS{
        String WX_SANDBOX="1";
    }

    interface WX_PAY_WAY{
        //扫码支付
        String Order_To_SnmBoss_TradeType_NATIVE="NATIVE";
        //app支付
        String Order_To_SnmBoss_TradeType_APP="APP";
        //H5支付 微信外
        String Order_To_SnmBoss_TradeType_H5_MWEB="MWEB";
        //H5支付 微信内
        String Order_To_SnmBoss_TradeType_H5_JSAPI="JSAPI";

    }


}
