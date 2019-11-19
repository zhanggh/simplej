package com.haven.simplej.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分表注解
 * 通过自定义分表策略实现分表，需要实现具体自定策略器需要实现TableShardingStrategy接口
 * @author haven.zhang
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TableShardingStrategy {
	/**
	 * 分表参数名
	 */
	String strategyBeanName();

	/**
	 * 分表参数
	 */
	String expression();
}
