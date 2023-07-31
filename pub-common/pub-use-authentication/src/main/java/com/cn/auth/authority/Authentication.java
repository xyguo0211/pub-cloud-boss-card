package com.cn.auth.authority;

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
