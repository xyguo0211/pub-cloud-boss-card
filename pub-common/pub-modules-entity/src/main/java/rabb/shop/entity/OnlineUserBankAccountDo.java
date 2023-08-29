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

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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

    /**
     * 邮箱验证码
     */
    @TableField(exist = false)
    private String emailCode;
}
