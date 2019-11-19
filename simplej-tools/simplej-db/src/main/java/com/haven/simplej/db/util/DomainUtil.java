package com.haven.simplej.db.util;

import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.exception.UncheckedException;
import com.vip.vjtools.vjkit.reflect.ReflectionUtil;

import java.util.function.Function;

/**
 * @author haven.zhang
 * @date 2019/1/16.
 */
public class DomainUtil {

	/**
	 * 判断是否存在对应的类属性名或表字段名（取决于valueFunc是哪个）
	 * @param values
	 * @param valueFunc
	 * @param name
	 * @return
	 */
	public static boolean isExistValue(EnumDomainDef[] values, Function<EnumDomainDef,String> valueFunc, String name) {
		for (EnumDomainDef value : values) {
			if (valueFunc.apply(value).equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 获取实体对象对应字段的定义
	 * @param clazz
	 * @param field object_field_name
	 * @param <T>
	 * @return
	 */
	public static <T extends BaseDomain> ColumnProperty getPropertyModel(Class<T> clazz, String field) {
		try {

			return ReflectionUtil.getField(clazz, field).getAnnotation(ColumnProperty.class);
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
	}
}
