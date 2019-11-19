package com.haven.simplej.rpc.annotation;

import java.lang.annotation.*;

/**
 * 用于指定某个对象或者字段是否为rpc接口必填属性
 * @author: havenzhang
 * @date: 2019/5/2 23:33
 * @version 1.0
 */
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcParam {

	/**
	 * 是否必填
	 * @return
	 */
	boolean required() default false;

	/**
	 * 字段别名
	 * @return
	 */
	String alias() default "";

	/**
	 * 校验正则表达式
	 * @return
	 */
	String regular() default "";

	/**
	 * 字符串的时候有效，默认是不限制
	 * @return
	 */
	int maxLength() default 0;

	/**
	 * 字符串最小长度
	 * @return
	 */
	int minLength() default 0;

	/**
	 * 默认值
	 * @return
	 */
	String defaultValus() default "";

}
