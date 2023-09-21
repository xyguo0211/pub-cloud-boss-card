package com.cn.school.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
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
@ExcelIgnoreUnannotated
public class TripOrderExcelDo {


    @ExcelProperty(value = "订单号",index = 0)   // 列索引为2
    private String orderId;
    /**
     * 学校名称
     */
    @ExcelProperty(value = "学校名称",index = 1)
    private String school;
    /**
     * 身份名称
     */
    @ExcelProperty(value = "姓名",index = 2)
    private String identityName;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号",index = 3)
    private String phone;

    /**
     * 票数
     */
    @ExcelProperty(value = "购买数量",index = 4)   // 列索引为2
    private Integer num;

    /**
     * 单价
     */
    @ExcelProperty(value = "单价",index = 5)   // 列索引为2
    private String price;

    /**
     * 总费用
     */
    @ExcelProperty(value = "总费用",index = 6)   // 列索引为2
    private String totalFee;

    /**
     * 订单状态   0 初始  1成功  -1 失败
     */
    @ExcelProperty(value = "订单状态",index = 7)   // 列索引为2
    private String status;


    @ExcelProperty(value = "创建时间",index = 8)   // 列索引为2
    private String createTime;


    /**
     *0 未上车  1 已上车1人  2 上车两人  n上车n人
     */
    @ExcelProperty(value = "上车人数",index = 9)
    private String onCarStatus;

    /**
     * 上车时间
     */
    @ExcelProperty(value = "上车时间",index = 10)
    private String oncarTime;


    @ExcelProperty(value = "车牌号",index = 11)
    private String carNumber;
    /**
     * 出发地
     */
    @ExcelProperty(value = "出发地",index = 12)
    private String origin;

    @ExcelProperty(value = "目的地",index = 13)
    private String destination;

    /**
     * 退款时间
     */
    @ExcelProperty(value = "退款时间",index = 14)
    private String refundTime;

    /**
     * 退款时间
     */
    @ExcelProperty(value = "退款金额",index = 15)
    private String refundFee;



}
