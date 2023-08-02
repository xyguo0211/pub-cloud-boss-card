package com.sn.online.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
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
@TableName("good_second_country")
public class GoodSecondCountryDo extends Model<GoodSecondCountryDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文案类容
     */
    private String countryName;

    private Date createTime;

    private Date updateTime;

    private String countryImage;

    private Integer firstId;

    @TableField(exist = false)
    private List<GoodThirdRateDo> listThird;

}
