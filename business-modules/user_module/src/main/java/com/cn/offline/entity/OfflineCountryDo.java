package com.cn.offline.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 菜单表 (EIP)
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-11
 */
@Data
@TableName("offline_country")
public class OfflineCountryDo extends Model<OfflineCountryDo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单名称
     */
    private String countryName;

    /**
     * 菜单路径
     */
    private String imageUrl;

    private Date createTime;

}
