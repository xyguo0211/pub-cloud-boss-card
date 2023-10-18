package rabb.shop.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import rabb.shop.enumschool.OnlineOrderStatusEnum;

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

    public String getOrderStatusStr() {
        if (Objects.isNull(orderStatus)) {
            return "";
        }
        return OnlineOrderStatusEnum.getOrderStatusStr(orderStatus);
    }

    @TableField(exist = false)
    private List<OnlineOrderInfoImageDo> listOrderInfoImage;

    @TableField(exist = false)
    private OnlineOrderInfoReplyDo onlineOrderInfoReplyDo;

    private Integer offlineUserId;
    private String offlineUserName;
    private Integer completeUserId;
    private String completeUserName;

    /**
     * 完成时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date completeTime;

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
     *    不需要  -1  审核中 0   审核完成9
     */
    private Integer  isInspect;

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
    private String transactionAmount;

    /**
     * 消息推送状态 0未推送  9推送成功
     */
    private Integer msgStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalAmonunt() {
        return totalAmonunt;
    }

    public void setTotalAmonunt(Integer totalAmonunt) {
        this.totalAmonunt = totalAmonunt;
    }

    public String getUserRemarks() {
        return userRemarks;
    }

    public void setUserRemarks(String userRemarks) {
        this.userRemarks = userRemarks;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getThirdId() {
        return thirdId;
    }

    public void setThirdId(Integer thirdId) {
        this.thirdId = thirdId;
    }

    public Integer getSecondId() {
        return secondId;
    }

    public void setSecondId(Integer secondId) {
        this.secondId = secondId;
    }

    public Integer getFirstId() {
        return firstId;
    }

    public void setFirstId(Integer firstId) {
        this.firstId = firstId;
    }

    public String getTotalAmonuntFee() {
        return totalAmonuntFee;
    }

    public void setTotalAmonuntFee(String totalAmonuntFee) {
        this.totalAmonuntFee = totalAmonuntFee;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OnlineOrderInfoImageDo> getListOrderInfoImage() {
        return listOrderInfoImage;
    }

    public void setListOrderInfoImage(List<OnlineOrderInfoImageDo> listOrderInfoImage) {
        this.listOrderInfoImage = listOrderInfoImage;
    }

    public OnlineOrderInfoReplyDo getOnlineOrderInfoReplyDo() {
        return onlineOrderInfoReplyDo;
    }

    public void setOnlineOrderInfoReplyDo(OnlineOrderInfoReplyDo onlineOrderInfoReplyDo) {
        this.onlineOrderInfoReplyDo = onlineOrderInfoReplyDo;
    }

    public Integer getOfflineUserId() {
        return offlineUserId;
    }

    public void setOfflineUserId(Integer offlineUserId) {
        this.offlineUserId = offlineUserId;
    }

    public String getOfflineUserName() {
        return offlineUserName;
    }

    public void setOfflineUserName(String offlineUserName) {
        this.offlineUserName = offlineUserName;
    }

    public Integer getCompleteUserId() {
        return completeUserId;
    }

    public void setCompleteUserId(Integer completeUserId) {
        this.completeUserId = completeUserId;
    }

    public String getCompleteUserName() {
        return completeUserName;
    }

    public void setCompleteUserName(String completeUserName) {
        this.completeUserName = completeUserName;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public String getCashBackFee() {
        return cashBackFee;
    }

    public void setCashBackFee(String cashBackFee) {
        this.cashBackFee = cashBackFee;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryImage() {
        return countryImage;
    }

    public void setCountryImage(String countryImage) {
        this.countryImage = countryImage;
    }

    public String getReplyFee() {
        return replyFee;
    }

    public void setReplyFee(String replyFee) {
        this.replyFee = replyFee;
    }

    public Integer getIsInspect() {
        return isInspect;
    }

    public void setIsInspect(Integer isInspect) {
        this.isInspect = isInspect;
    }

    public String getInspectFee() {
        return inspectFee;
    }

    public void setInspectFee(String inspectFee) {
        this.inspectFee = inspectFee;
    }

    public String getInspectUserName() {
        return inspectUserName;
    }

    public void setInspectUserName(String inspectUserName) {
        this.inspectUserName = inspectUserName;
    }

    public Integer getInspectUserId() {
        return inspectUserId;
    }

    public void setInspectUserId(Integer inspectUserId) {
        this.inspectUserId = inspectUserId;
    }

    public Date getInspectCompleteTime() {
        return inspectCompleteTime;
    }

    public void setInspectCompleteTime(Date inspectCompleteTime) {
        this.inspectCompleteTime = inspectCompleteTime;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Integer getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(Integer msgStatus) {
        this.msgStatus = msgStatus;
    }
}
