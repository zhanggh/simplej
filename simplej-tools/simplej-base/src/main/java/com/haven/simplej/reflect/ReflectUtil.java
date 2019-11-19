package com.haven.simplej.reflect;


import com.vip.vjtools.vjkit.reflect.ReflectionUtil;

import java.lang.reflect.*;

/**
 * 反射工具类
 * @author: havenzhang
 * @date: 2019/1/22 17:39
 * @version 1.0
 */
public class ReflectUtil extends ReflectionUtil {

	/**
	 * 获取方法返回类型的泛型
	 * @param method 方法对象
	 * @return Type
	 */
	public static Type[] getGenericReturnType(Method method) {
		Type gType = method.getGenericReturnType();
		Type[] returnTypes = null;
		// 如果gType类型是ParameterizedType对象
		if (gType instanceof ParameterizedType) {
			// 强制类型转换
			ParameterizedType pType = (ParameterizedType) gType;
			// 获取原始类型
			Type rType = pType.getRawType();
			// 取得泛型类型的泛型参数
			returnTypes = pType.getActualTypeArguments();

		}
		return returnTypes;
	}

	/**
	 * 通过常量属性名字,在字节码中获取到一个常量的值
	 * 这个方法常用于取出接口中常量的值,可以适用于接口中的常量;类中的常量,但是仅限于常量.如果是变量,返回为空
	 * 注意: 如果你要取出来的不是常量,我也给你返回了null,这是为了确保,取出来的一定是 final 的常量
	 * @param   fieldName  属性的名字
	 * @param   clazz   在那个字节码中
	 * @return 如果找找到, 并且他也是常量, 我就返回它的值, 否则返回null
	 * @throws Exception 反射的风险异常.
	 */
	public static Object getFinalFieldValue(Class<?> clazz, String fieldName) throws Exception {
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			//确保你要取的,是常量.否则,就不用进去这个获取操作了
			boolean isFinal = Modifier.isFinal(field.getModifiers());
			if (isFinal) {
				String name = field.getName();
				if (fieldName.equals(name)) {
					return field.get(null);
				}
			}
		}
		return null;
	}
}
