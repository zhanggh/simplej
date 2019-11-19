package com.haven.simplej.db.annotation;

import com.haven.simplej.db.conditoin.ShardingDataSourceCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

/**
 * @author haven.zhang
 * @date 2019/1/27.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@HasDatasourceConditon
@Conditional(ShardingDataSourceCondition.class)
public @interface ShardingConditon {
	@AliasFor(annotation = HasDatasourceConditon.class)
	String value() default "";
}
