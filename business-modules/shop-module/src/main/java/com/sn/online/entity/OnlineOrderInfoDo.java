package com.sn.online.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-02
 */
@Data
@TableName("online_order_info")
public class OnlineOrderInfoDo extends Model<OnlineOrderInfoDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer totalAmonunt;

    private String userRemarks;

    private Date createTime;

    private Integer userId;

    private Integer thirdId;

    private Integer secondId;

    private Integer firstId;

    private String totalAmonuntFee;

    private String rate;

    private Integer orderStatus;

    @ApiModelProperty("跟踪状态文本")
    public String getOrderStatusStr() {
        if (Objects.isNull(orderStatus)) {
            return "";
        }
        return OrderStatusEnum.getOrderStatusStr(orderStatus);
    }

    @TableField(exist = false)
    private List<OnlineOrderInfoImageDo> listOrderInfoImage;

    @TableField(exist = false)
    private OnlineOrderInfoReplyDo onlineOrderInfoReplyDo;


}
