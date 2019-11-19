package com.haven.simplej.authen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制类或者方法上加入这个注解，该方法的访问进行日志登记
 * @author havenzhang
 *
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLog {
	String value() default "";
}
