package com.cn.offline.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Data
@TableName("offline_role_menu")
public class OfflineRoleMenuDo extends Model<OfflineRoleMenuDo> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer roleId;

    private Integer menuId;

    private Date createTime;



}
