package com.cn.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 菜单表 (EIP)
 * </p>
 *
 * @author ganyongheng
 * @since 2022-08-18
 */
@TableName("sys_menu")
public class SysMenuDo extends Model<SysMenuDo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单路径
     */
    private String menuUrl;

    /**
     * 菜单上级id
     */
    private Long parentId;

    /**
     * 所有功能值
     */
    private String funValue;

    /**
     * 功能名称
     */
    private String funName;

    /**
     * 排序
     */
    private Integer sortno;

    /**
     * 1表示使用，2表示禁用
     */
    private Integer type;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getFunValue() {
        return funValue;
    }

    public void setFunValue(String funValue) {
        this.funValue = funValue;
    }

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public Integer getSortno() {
        return sortno;
    }

    public void setSortno(Integer sortno) {
        this.sortno = sortno;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }



    @Override
    public String toString() {
        return "SysMenuDo{" +
        "id=" + id +
        ", menuName=" + menuName +
        ", menuUrl=" + menuUrl +
        ", parentId=" + parentId +
        ", funValue=" + funValue +
        ", funName=" + funName +
        ", sortno=" + sortno +
        ", type=" + type +
        "}";
    }
}
