package com.cn.offline.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pub.core.common.OrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 完成时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date completeTime;

    /**
     * 下单人的id
     */
    private Integer userId;
    /**
     * 下单人的姓名
     */
    private String userName;

    private Integer thirdId;

    private Integer secondId;

    private Integer firstId;

    private String totalAmonuntFee;

    private String rate;

    private Integer orderStatus;

    private Integer offlineUserId;

    private String offlineUserName;

    private Integer completeUserId;

    private String completeUserName;

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

    private String cashBackFee;


    @TableField(exist = false)
    private String  cardName;

    @TableField(exist = false)
    private String  cardImage;

    @TableField(exist = false)
    private String  countryName;

    @TableField(exist = false)
    private String  countryImage;
    /**
     *客服回复金额
     */
    private String  replyFee;

    /**
     *   不需要  -1  审核中 0   审核完成9
     */
    private Integer  isInspect;

    public String getIsInspectStr() {
        if (Objects.isNull(isInspect)) {
            return "";
        }
        return OrderStatusEnum.getIsInspectStr(isInspect);
    }

    /**
     * 审核人员回复金额
     */
    private String inspectFee;
    /**
     * 审核人员
     */
    private String inspectUserName;
    /**
     * 审核人员id
     */
    private Integer inspectUserId;
    /**
     * 审核完成时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inspectCompleteTime;


    /**
     * 最终交易金额
     */
    String transactionAmount;

    /**
     * 消息推送状态 0未推送  9推送成功
     */
    Integer msgStatus;
}
