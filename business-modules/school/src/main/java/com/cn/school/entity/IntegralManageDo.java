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

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 车次
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-16
 */
@Data
@TableName("integral_manage")
public class IntegralManageDo extends Model<IntegralManageDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private String integral;

    private String integralFee;
    //0 初始化   1成功
    private Integer status;

    @ApiModelProperty("體現状态")
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

    private String identityName;

    private String phone;

    private Integer sysUserId;

    private String sysIdentityName;





}
