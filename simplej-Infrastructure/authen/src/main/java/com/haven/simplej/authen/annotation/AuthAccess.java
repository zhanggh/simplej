package com.haven.simplej.authen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制类的方法上加入这个注解，该方法的访问则需要权限控制
 * @author havenzhang
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@LoginAccess
public @interface AuthAccess{
	String value() default "";
}