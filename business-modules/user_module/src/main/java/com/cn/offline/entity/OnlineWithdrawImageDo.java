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
 * 提现管理
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-14
 */
@Data
@TableName("online_withdraw_image")
public class OnlineWithdrawImageDo extends Model<OnlineWithdrawImageDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private String imageUrl;

    private Integer parentId;


}
