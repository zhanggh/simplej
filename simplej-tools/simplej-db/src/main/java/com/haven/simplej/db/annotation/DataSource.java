package com.haven.simplej.db.annotation;


import com.haven.simplej.db.constant.Constant;

import java.lang.annotation.*;

/**
 * 加上该主键的类or方法，代表执行数据库操作的数据源是指定DataSource下的数据源
 * @author haven.zhang
 * @date 2019/1/26.
 */
@Target({ ElementType.METHOD , ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DataSource {


	/**
	 * 数据源配置的唯一id
	 */
	String id() default "";


	/**
	 * 库名,当我们实现DBShardingStrategy接口的时候，不需要填该字段值，由策略生成对应的dbName
	 */
	String dbName() default "";

	/**
	 * 库分组名,当我们实现DBShardingStrategy接口的时候，不需要填该字段值，由策略生成对应的groupName
	 */
	String groupName() default Constant.DEFAULT_DATA_SOURCE_GROUP_NAME;
}
