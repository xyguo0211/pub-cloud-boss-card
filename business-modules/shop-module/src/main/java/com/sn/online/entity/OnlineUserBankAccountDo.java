package com.sn.online.entity;

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
 * 用户银行卡管理
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-06
 */
@Data
@TableName("online_user_bank_account")
public class OnlineUserBankAccountDo extends Model<OnlineUserBankAccountDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行卡号
     */
    private String bankAccountNumber;

    private Date createTime;

    /**
     * 银行卡持有者姓名
     */
    private String bankAccountName;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 登录姓名
     */
    private String loginName;

    /**
     * 9未删除  -1已删除
     */
    private Integer deleteStatus;


}
