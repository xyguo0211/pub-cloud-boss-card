package com.cn.school.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 行程表
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-16
 */
@Data
@TableName("trip_area")
public class TripAreaDo extends Model<TripAreaDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 出发地
     */
    private String origin;

    private String destination;

    /**
     * 目的地
     */
    private Date createTime;

    /**
     * 是否热门 0 否 1是  
     */
    private Integer isHot;

    private String cityName;


}
