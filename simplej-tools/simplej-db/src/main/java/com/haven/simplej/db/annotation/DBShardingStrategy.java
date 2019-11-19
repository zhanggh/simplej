package com.haven.simplej.db.annotation;


import com.haven.simplej.db.constant.Constant;

import java.lang.annotation.*;

/**
 * 分库注解
 *
 * @author haven.zhang
 *
 */
@Target({ ElementType.METHOD ,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DBShardingStrategy {
	/**
	 * 分库策略名 如果策略名为空，则dbName必须不能为空，否则抛出异常
	 * 如果使用自定义策略，key也必须有值
	 */
	String strategyBeanName() default "";

	/**
	 * 分库参数，当我们实现DBShardinexpressiongStrategy接口的时候，
	 * 通过key指定el表达式生成参数，在策略实现类中获取参数值，并计算对应的分组与dbName
	 */
	String expression() default "";


}
