package com.cn.school.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-14
 */
@Data
@TableName("user")
public class UserDo extends Model<UserDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String openid;

    private String wxunionid;

    /**
     * 0 黑名单 9 否
     */
    private Integer isDelete;

    private String nickName;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String school;

    private String identityName;

    private String phone;

    /**
     * 手机验证码
     */
    @TableField(exist = false)
    private String phoneCode;
//1 系统管理员  2 扫描人员  3普通用户
    private Integer roleId;

    /**
     * 邀请人的openid
     */
    private String invitationOpenid;
    /**
     * 积分
     */
    private String integral;
    /**
     * 是否已通知  0 初始化  1 已通知
     */
    private Integer noticeStatus;

}
