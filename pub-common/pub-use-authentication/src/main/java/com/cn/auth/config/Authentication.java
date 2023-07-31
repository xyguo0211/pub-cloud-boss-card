package com.cn.auth.config;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication {


	boolean validate() default true;


	String menu() default "";


	AuthorityType[] type() default AuthorityType.NON;

}
