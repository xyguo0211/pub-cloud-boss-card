package com.cn.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

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

    private Date createTime;

    private Date updateTime;

    /**
     * 是否黑名单  -1 黑名单  9 白名单
     */
    private Integer isBlack;

    /**
     * 邀请码
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
