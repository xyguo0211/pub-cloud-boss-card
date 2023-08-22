package com.sn.online.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pub.core.common.OrderStatusEnum;
import com.sn.online.config.OnlineOrderStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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

    public String getTransactionTypeStr() {
        if (Objects.isNull(type)) {
            return "";
        }
        return OnlineOrderStatusEnum.getTransactionTypeStr(type);
    }

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

    private String cashBackFee;

    private Integer withdrawId;
}
