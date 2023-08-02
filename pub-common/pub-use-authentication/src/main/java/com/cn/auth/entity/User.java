package com.cn.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import javax.xml.crypto.Data;
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
@TableName("sys_user")
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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }



    @Override
    public String toString() {
        return "SysUserDo{" +
        "id=" + id +
        ", loginName=" + loginName +
        ", phoneNumber=" + phoneNumber +
        ", password=" + password +
        ", userType=" + userType +
        ", loginIp=" + loginIp +
        ", loginDate=" + loginDate +
        ", updateDate=" + updateDate +
        ", status=" + status +
        "}";
    }
}
