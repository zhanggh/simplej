package com.haven.simplej.rpc.util;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.reflect.ReflectionUtil;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

/**
 * @author: havenzhang
 * @date: 2019/9/22 17:39
 * @version 1.0
 */
public class ReflectUtil extends ReflectionUtil {


	/**
	 * 方法入参参数泛型信息
	 */
	private static Map<Method, List<Type[]>> paramGenericTypeCache = Maps.newConcurrentMap();
	/**
	 * 方法返回参数泛型信息
	 */
	private static Map<Method, Type[]> returnGenericTypeCache = Maps.newConcurrentMap();

	/**
	 * 获取方法参数的泛型信息
	 * @param method 方法对象
	 * @return List<Type               [               ]>
	 */
	public static List<Type[]> getParamGenericType(Method method) {
		List<Type[]> list = paramGenericTypeCache.get(method);
		if (CollectionUtil.isNotEmpty(list)) {
			return list;
		}
		Type[] types = method.getGenericParameterTypes();
		Type[] paramTypes = null;
		list = Lists.newArrayList();
		for (Type type : types) {
			// 如果gType类型是ParameterizedType对象
			if (type instanceof ParameterizedType) {
				// 强制类型转换
				ParameterizedType pType = (ParameterizedType) type;
				// 获取原始类型
				Type rType = pType.getRawType();
				// 取得泛型类型的泛型参数
				paramTypes = pType.getActualTypeArguments();
				list.add(paramTypes);
			} else {
				list.add(null);
			}
		}
		paramGenericTypeCache.put(method, list);
		return list;
	}

	/**
	 * 获取方法返回类型的泛型
	 * @param method 方法对象
	 * @return Type
	 */
	public static Type[] getGenericReturnType(Method method) {
		Type[] returnTypes = returnGenericTypeCache.get(method);
		if (returnTypes != null && returnTypes.length > 0) {
			return returnTypes;
		}

		if (returnTypes != null && returnTypes.length == 0) {
			return null;
		}
		Type gType = method.getGenericReturnType();
		// 如果gType类型是ParameterizedType对象
		if (gType instanceof ParameterizedType) {
			// 强制类型转换
			ParameterizedType pType = (ParameterizedType) gType;
			// 获取原始类型
			Type rType = pType.getRawType();
			// 取得泛型类型的泛型参数
			returnTypes = pType.getActualTypeArguments();

		}
		if (returnTypes != null) {
			returnGenericTypeCache.put(method, returnTypes);
		} else {
			returnGenericTypeCache.put(method, new Type[0]);
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
