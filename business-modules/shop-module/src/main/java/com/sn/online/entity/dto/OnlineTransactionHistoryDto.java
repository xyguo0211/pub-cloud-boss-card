package com.sn.online.entity.dto;

import com.sn.online.config.OnlineOrderStatusEnum;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
public class OnlineTransactionHistoryDto {

    private Integer id;

    private String totalAmonunt;

    private String cardName;

    private String bankName;

    private String thirdUserName;

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

    private Date createTime;
}
