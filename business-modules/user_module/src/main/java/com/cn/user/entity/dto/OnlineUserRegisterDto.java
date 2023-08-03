package com.cn.user.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

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
    /**
     * 昵称不能为空
     */
    @NotBlank(message = "   Nickname cannot be empty   !")
    private String nikeName;


}
