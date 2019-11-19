package com.haven.simplej.db.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 分库和分表策略注解
 * 加上该注解的类，代表一个分库or分表策略类
 * @author haven.zhang
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Strategy {
	@AliasFor(
			annotation = Component.class
	)
	/**
	 * 策略名
	 */
	String value();
}
