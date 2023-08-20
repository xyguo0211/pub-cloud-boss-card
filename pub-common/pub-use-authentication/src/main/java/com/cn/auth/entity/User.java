package com.cn.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 登录帐号表
 * </p>
 *
 * @author ganyongheng
 * @since 2022-08-18
 */
@Data
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 登录名
     */
    private String loginName;
    /**
     * 昵称
     */
    private String nikeName;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户类型(0系统用户）
     */
    private Integer userType;

    /**
     * 最后登陆IP
     */
    private String loginIp;

    /**
     * 最后登陆时间
     */
    private Date loginDate;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 状态(0禁用，1启用)
     */
    private Integer status;

    /**
     * 登录有效期
     */
    private Date startDate;

    /**
     * 登录失效时间
     */
    private Date endDate;


}
