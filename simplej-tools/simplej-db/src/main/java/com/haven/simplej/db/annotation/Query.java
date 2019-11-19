package com.haven.simplej.db.annotation;


import com.haven.simplej.db.constant.Constant;

import java.lang.annotation.*;

/**
 * 查询分组
 * @author haven.zhang
 * @date 2019/1/26.
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Query {
	/**
	 * 如：user_${0}...${9}
	 * @return String
	 */
	String tableExpression() default "";
	/**
	 * 如：test_${00}...${99}
	 * @return String
	 */
	String dbNameExpression() default "";

	/**
	 * 数据源配置文件中指定的分组名
	 * @return String
	 */
	String groupName() default Constant.DEFAULT_DATA_SOURCE_GROUP_NAME;

	/**
	 * 是否从主库查询
	 * @return
	 */
	boolean isMaster() default true;
}
