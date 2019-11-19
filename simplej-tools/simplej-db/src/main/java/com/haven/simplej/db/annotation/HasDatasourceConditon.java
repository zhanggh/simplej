package com.haven.simplej.db.annotation;

import com.haven.simplej.db.conditoin.HasDataSourceCondition;
import com.haven.simplej.db.conditoin.ShardingDataSourceCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 是否有数据源的配置
 * @author haven.zhang
 * @date 2019/1/27.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(HasDataSourceCondition.class)
public @interface HasDatasourceConditon {
	String value() default "";
}
