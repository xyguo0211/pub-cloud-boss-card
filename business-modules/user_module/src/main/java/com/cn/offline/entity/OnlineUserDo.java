package com.cn.offline.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pub.core.common.OfflineStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 在线用户表
 * </p>
 *
 * @author ganyongheng
 * @since 2023-07-31
 */
@Data
@TableName("online_user")
public class OnlineUserDo extends Model<OnlineUserDo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String pwd;

    private Date createTime;

    private Date updateTime;

    /**
     * 是否黑名单  -1 黑名单  9 白名单
     */
    private Integer isBlack;

    @ApiModelProperty("黑名单")
    public String isBlackStr() {
        if (Objects.isNull(isBlack)) {
            return "";
        }
        if(9==isBlack){
            return "正常";
        }else{
            return "黑名单";
        }

    }

    /**
     * 邀请码
     */
    private String randomCode;
    /**
     * 邀请人
     */
    @TableField(exist = false)
    private String randomCodeUse;
    /**
     * 我的邀请码
     */
    private String myInvitationCode;
    /**
     * 余额
     */
    private String balance;
    /**
     * 1 系统人员   2 非系统人员
     */
    private Integer role;

    @ApiModelProperty("角色文案")
    public String isRoleStr() {
        if (Objects.isNull(role)) {
            return "";
        }
        if(1==role){
            return "系统人员";
        }else{
            return "客户";
        }

    }


    private String nikeName;

}
