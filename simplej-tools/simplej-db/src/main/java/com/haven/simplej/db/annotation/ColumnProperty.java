package com.haven.simplej.db.annotation;

import java.lang.annotation.*;

/**
 * 数据库列属性
 * @author  haven.zhang
 * @date 2018/12/30.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ColumnProperty {
	/**
	 * db列名
	 * @return
	 */
	String name() default "";

	/**
	 * 是否唯一
	 * @return
	 */
	boolean unique() default false;

	/**
	 * 是否可空
	 * @return
	 */
	boolean nullable() default false;

	/**
	 * 是否可插入
	 * @return
	 */
	boolean insertable() default true;

	/**
	 * 是否可更新
	 * @return
	 */
	boolean updatable() default true;

	/**
	 * 字段备注（说明）
	 * @return
	 */
	String comment() default "";

	String columnDefinition() default "";

	String table() default "";

	/**
	 * 默认值 对应varchar 类型有效
	 * @return
	 */
	String defaultValue() default "";

	/**
	 * 字段长度
	 * @return
	 */
	int length() default 255;

	/**
	 * 精度
	 * @return
	 */
	int precision() default 0;

	/**
	 *
	 * @return
	 */
	int scale() default 0;

	/**
	 * 数据类型
	 * @return
	 */
	String dataType() default "varchar";
}
