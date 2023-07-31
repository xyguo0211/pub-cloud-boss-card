package com.cn.user.entity;



import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author howlingpan
 * @createdAt on 2017-06-16
 */
public class LoginVM {
    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    private String password;
    //1 表示2.0系统登录 2表示标准化服务平台登录
    private Integer type;

    public Integer getType() {
        if(type == null){
            type = 1;
        }
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
