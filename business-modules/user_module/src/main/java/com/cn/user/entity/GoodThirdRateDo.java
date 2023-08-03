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
@TableName("good_third_rate")
public class GoodThirdRateDo extends Model<GoodThirdRateDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 费率
     */
    private String rate;

    private Date createTime;

    private Date updateTime;

    private Integer firstId;

    private Integer secondId;

    /**
     * 费用开始区间
     */
    private String needStart;

    /**
     * 费用结束区间
     */
    private String needEnd;

    /**
     * 备注说明
     */
    private String requisite;

    private Integer needWaitTime;


    @TableField(exist = false)
    private List<GoodThirdCardTypeDo> listCardType;

    @TableField(exist = false)
    private GoodSecondCountryDo goodSecondCountryDo;

    @TableField(exist = false)
    private GoodFirstMeumDo goodFirstMeumDo;

}
