package com.sn.online.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * <p>
 * 注册接收实体类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-07-31
 */
@Data
public class OnlineUserRegisterDto {



    /**
     * 用户名
     */
    @NotBlank(message = " User name cannot be empty !")
    private String name;

    /**
     * 密码
     */
    @NotBlank(message = " Password cannot be empty !")
    @Length(message = " The password cannot exceed or be less than  {min} !", min = 6)
    private String pwd;

    /**
     * 邀请码
     */
    @NotBlank(message = " Invitation code cannot be empty !")
    private String randomCode;
    /**
     * 邮箱验证码
     */
    @NotBlank(message = "  Email verification code cannot be empty  !")
    private String emailCode;


}
