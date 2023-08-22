package com.cn.offline.entity;

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
 * 交易记录
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-12
 */
@Data
@TableName("online_transaction_history")
public class OnlineTransactionHistoryDo extends Model<OnlineTransactionHistoryDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 交易类型  1 售卡  2 邀请码奖励  3 提现
     */
    private Integer type;

    private String totalAmonunt;

    /**
     * 产品id
     */
    private Integer thirdId;

    /**
     * 用户id
     */
    private Integer userId;

    private Date createTime;

    /**
     * 关联的订单号
     */
    private Integer orderId;

    /**
     * 返现用户id
     */
    private Integer thirdUserId;

    private String thirdUserName;

    /**
     * 邀请码返现额度
     */
    private String cashBackFee;

    /**
     * 提现的id
     */
    private Integer withdrawId;
}
