package com.cn.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-02
 */
@Data
@TableName("good_first_meum")
public class GoodFirstMeumDo extends Model<GoodFirstMeumDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Date updateTime;

    private String cardImgeUrl;


    private String cardName;

    @TableField(exist = false)
    private List<GoodSecondCountryDo> listSencond;

    @TableField(exist = false)
    private List<GoodFirstMeumEquirementsDo> listEquirements;
}
