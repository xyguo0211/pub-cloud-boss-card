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
 * 订单图片回复表
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Data
@TableName("online_order_info_reply")
public class OnlineOrderInfoReplyDo extends Model<OnlineOrderInfoReplyDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Integer orderId;

    /**
     * 回复客服id
     */
    private Integer replyUserId;

    private String replyUserName;

    /**
     * 回复信息
     */
    private String replyMsg;

    @TableField(exist = false)
    List<OnlineOrderInfoReplyImageDo> listOnlineOrderInfoReplyImageDo;

}
