package com.cn.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 角色菜单关系表
 * </p>
 *
 * @author ganyongheng
 * @since 2022-08-18
 */
@TableName("sys_role_menu")
public class SysRoleMenuDo extends Model<SysRoleMenuDo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 功能值
     */
    private String funValue;

    /**
     * 角色id
     */
    private Long roleId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getFunValue() {
        return funValue;
    }

    public void setFunValue(String funValue) {
        this.funValue = funValue;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }



    @Override
    public String toString() {
        return "SysRoleMenuDo{" +
        "id=" + id +
        ", menuId=" + menuId +
        ", funValue=" + funValue +
        ", roleId=" + roleId +
        "}";
    }
}
