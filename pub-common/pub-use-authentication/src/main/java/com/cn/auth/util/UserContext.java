package com.cn.auth.util;


import com.cn.auth.entity.User;

/**
 * Created by zyang on 2017/6/24 0024.
 */
public class UserContext {

    static final ThreadLocal<User> current = new ThreadLocal<User>();

    public UserContext(User user) {
        current.set(user);
    }

    public static User getCurrentUser() {
        return current.get();
    }

    public void close() {
        current.remove();
    }
}
