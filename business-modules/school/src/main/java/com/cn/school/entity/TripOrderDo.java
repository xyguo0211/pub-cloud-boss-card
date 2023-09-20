package com.cn.school.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.cn.school.config.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-16
 */
@Data
@TableName("trip_order")
public class TripOrderDo extends Model<TripOrderDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String orderId;

    /**
     * 票数
     */
    private Integer num;

    /**
     * 单价
     */
    private String price;

    /**
     * 总费用
     */
    private String totalFee;

    /**
     * 订单状态   0 初始  1成功  -1 失败
     */
    private Integer status;

    @ApiModelProperty("订单状态")
    public String isStatusStr() {
        if (Objects.isNull(status)) {
            return "";
        }
        if(Constant.OrderStatus.WAIT==status){
            return "待支付";
        }else if(Constant.OrderStatus.SUCESS==status){
            return "支付成功";
        }else if(Constant.OrderStatus.REFUND ==status){
            return "已退款";
        }else{
            return "支付失败";
        }
    }

    /**
     * 发车时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date starTime;

    /**
     * 产品id
     */
    private Integer productId;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(exist = false)
    private String createTimeStr;
    /**
     * 支付时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**
     *0 未上车  1 已上车1人  2 上车两人  n上车n人
     */
    private Integer onCarStatus;

    @ApiModelProperty("上车人数")
    public String onCarStatusStr() {
        if (Objects.isNull(onCarStatus)) {
            return "";
        }
        if(0==onCarStatus){
            return "未上车";
        }else{
            return "已上车"+onCarStatus+"人";
        }
    }


    /**
     * 车次
     */
    private Integer carId;
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 车到时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 出发地
     */
    private String origin;

    private String destination;

    /*
    微信支付系统生成的订单号。
     */
    private String transactionId;

    /**
     * 是否释放车票   9已退票  -1 未退票
     */
    private Integer ticketStatus;



    /**
     * 上车时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date oncarTime;


    /**
     * 学校名称
     */
    private String school;

    /**
     * 身份名称
     */
    private String identityName;

    /**
     * 手机号
     */
    private String phone;
    /**
     * 退款订单号
     */
    private String refundOrderId;
    /**
     * 退款订单号
     */
    private String refundFee;
    /**
     * 退款时间
     */
    private Date refundTime;

    //发车前通知  9 已通知  -1 未通知
    private  Integer noticeStatus;

}
