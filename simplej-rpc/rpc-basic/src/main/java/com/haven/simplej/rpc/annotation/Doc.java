package com.haven.simplej.rpc.annotation;

import java.lang.annotation.*;

/**
 * 注释文档
 * @author: havenzhang
 * @date: 2019/8/15 23:27
 * @version 1.0
 */
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Doc {
	String value() default "";
	String date() default "";
	String author() default "";
}
