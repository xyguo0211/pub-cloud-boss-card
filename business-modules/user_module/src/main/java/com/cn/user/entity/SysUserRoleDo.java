package com.cn.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author ganyongheng
 * @since 2022-08-18
 */
@TableName("sys_user_role")
public class SysUserRoleDo extends Model<SysUserRoleDo> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 角色编号
     */
    private Long roleId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }



    @Override
    public String toString() {
        return "SysUserRoleDo{" +
        "userId=" + userId +
        ", roleId=" + roleId +
        "}";
    }
}
