package com.cn.auth.config;

import java.lang.annotation.*;

/**
 * 方法执行时间统计用注解
 *
 * @author Michael
 * @version 1.0
 * @since 2023/3/22
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimingLog {
}
