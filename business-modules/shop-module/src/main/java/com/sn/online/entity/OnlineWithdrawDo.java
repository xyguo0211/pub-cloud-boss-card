package com.sn.online.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pub.core.common.OrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 提现管理
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-06
 */
@Data
@TableName("online_withdraw")
public class OnlineWithdrawDo extends Model<OnlineWithdrawDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Integer bankId;

    /**
     * 提现金额
     */
    private String drawalFee;

    private Integer userId;

    /**
     * 提现前金额
     */
    private String beforeDrawalFee;

    /**
     * 提现后金额
     */
    private String aftherDrawalFee;

    /**
     * 状态  9成功  -1失败  0 取消  1 审核中
     */
    private Integer status;

    @ApiModelProperty("跟踪状态文本")
    public String getDrawalFeeStatusStr() {
        if (Objects.isNull(status)) {
            return "";
        }
        return OrderStatusEnum.getDrawalFeeStatusStr(status);
    }

    /**
     * 取消原因，失败原因
     */
    private String msg;


}
