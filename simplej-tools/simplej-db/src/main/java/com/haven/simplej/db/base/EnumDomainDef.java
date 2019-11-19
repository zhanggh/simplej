package com.haven.simplej.db.base;

import com.haven.simplej.db.annotation.ColumnProperty;

/**
 * 数据库实体对象字段定义
 *
 */
public interface EnumDomainDef {

	/**
	 * 获取属性名，无需重写，Java 枚举类自带此方法
	 * @return
	 */
	String name();

	/**
	 * 获取属性名，请尽量使用对应枚举类的 Enum.XXX.name() 代替此方法
	 * @return
	 */
	default String getProperty() {
		return name();
	}

	/**
	 * 获取描述（备注）
	 * @return
	 */
	default String getDesc() {
		return getPropertyModel().comment();
	}

	/**
	 * 获取长度
	 * @return
	 */
	default int getLength() {
		return getPropertyModel().length();
	}

	/**
	 * 是否可为空 0=not null,1=null
	 *
	 * @return
	 */
	default boolean getNullable() {
		return getPropertyModel().nullable();
	}

	/**
	 * 数据库字段默认值，如果没有设置 用常量db_null代替
	 *
	 * @return
	 */
	default String getColumnDef() {
		return getPropertyModel().defaultValue();
	}

	/**
	 * 获取字段属性
	 * @return
	 */
	default ColumnProperty getPropertyModel(){
		return null;
	}

	default String[] getFields(){
		return null;
	}
}