package com.cn.school.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

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
    /**
     * 支付时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**
     * 0 未上车  1 已上车  -1 已过期 
     */
    private Integer onCarStatus;

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
     * 是否已退票   9已退票  -1 未退票
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

}
