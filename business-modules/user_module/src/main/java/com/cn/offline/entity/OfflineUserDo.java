package com.cn.offline.entity;

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
 * 离线用户表
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Data
@TableName("offline_user")
public class OfflineUserDo extends Model<OfflineUserDo> {

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

    /**
     * 1 系统人员   2 非系统人员
     */
    private Integer roleId;

    /**
     * 客服开始工作时间
     */
    private Date startTime;

    /**
     * 客服结束工作时间,期间不允许登录
     */
    private Date endTime;


}
