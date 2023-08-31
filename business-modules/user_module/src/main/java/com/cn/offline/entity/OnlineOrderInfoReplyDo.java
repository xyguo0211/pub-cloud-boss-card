package com.cn.offline.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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

    private Integer status;

    /**
     * 回复金额
     */
    private String replyFee;

}
