package com.sn.online.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 在线用户表
 * </p>
 *
 * @author ganyongheng
 * @since 2023-07-31
 */
@Data
@TableName("online_user")
public class OnlineUserDo extends Model<OnlineUserDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String pwd;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Date updateTime;

    /**
     * 是否黑名单  -1 黑名单  9 白名单
     */
    private Integer isBlack;

    /**
     * 邀请码checkIdentity
     */
    private String randomCode;
    /**
     * 我的邀请码
     */
    private String myInvitationCode;
    /**
     * 余额
     */
    private String balance;
    /**
     * 1 系统人员   2 非系统人员
     */
    private Integer role;

    private String nikeName;

}
