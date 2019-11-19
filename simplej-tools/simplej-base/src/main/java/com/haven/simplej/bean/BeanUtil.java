package com.haven.simplej.bean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.collection.MapUtil;

import com.vip.vjtools.vjkit.reflect.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
public class BeanUtil {

	public static PropertyDescriptor findPD(PropertyDescriptor[] pda, String name) {
		int i;
		for (i = 0; i < pda.length; ++i) {
			PropertyDescriptor pd = pda[i];
			if (pd.getName().equals(name))
				return pd;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static PropertyDescriptor[] getPDs(Class clz) {
		BeanInfo bi = null;
		try {
			bi = Introspector.getBeanInfo(clz);
		} catch (Exception e) {
			return new PropertyDescriptor[0];
		}
		PropertyDescriptor[] descriptors;
		descriptors = bi.getPropertyDescriptors();
		if (descriptors == null)
			return new PropertyDescriptor[0];
		return descriptors;
	}

	@SuppressWarnings("unchecked")
	public static boolean isDigit(Class clz) {
		if (clz.isAssignableFrom(Character.class))
			return true;
		else if (clz.isAssignableFrom(Byte.class))
			return true;
		else if (clz.isAssignableFrom(Short.class))
			return true;
		else if (clz.isAssignableFrom(Integer.class))
			return true;
		else if (clz.isAssignableFrom(Long.class))
			return true;
		else if (clz.isAssignableFrom(Float.class))
			return true;
		else if (clz.isAssignableFrom(Double.class))
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public static Object toObj(Class clz, long val) {
		if (clz.isAssignableFrom(Character.class))
			return new Character((char) val);
		else if (clz.isAssignableFrom(Byte.class))
			return new Byte((byte) val);
		else if (clz.isAssignableFrom(Short.class))
			return new Short((short) val);
		else if (clz.isAssignableFrom(Integer.class))
			return new Integer((int) val);
		else if (clz.isAssignableFrom(Long.class))
			return new Long(val);
		else if (clz.isAssignableFrom(Float.class))
			return new Float(val);
		else if (clz.isAssignableFrom(Double.class))
			return new Double(val);
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Object getMin(Class clz) {
		if (clz.isAssignableFrom(Character.class))
			return new Character(Character.MIN_VALUE);
		else if (clz.isAssignableFrom(Byte.class))
			return new Byte(Byte.MIN_VALUE);
		else if (clz.isAssignableFrom(Short.class))
			return new Short(Short.MIN_VALUE);
		else if (clz.isAssignableFrom(Integer.class))
			return new Integer(Integer.MIN_VALUE);
		else if (clz.isAssignableFrom(Long.class))
			return new Long(Long.MIN_VALUE);
		else if (clz.isAssignableFrom(Float.class))
			return new Float(Float.MIN_VALUE);
		else if (clz.isAssignableFrom(Double.class))
			return new Double(Double.MIN_VALUE);
		//else if(clz.isAssignableFrom(Boolean.class)) return Boolean.
		return null;
	}

	/**
	 * 属性拷贝，从map拷贝到clz 实例
	 * @param source
	 * @param clz
	 * @return
	 */
	public static <T> T copyFromMap(Map<String, Object> source, Class clz) {

		try {
			return copyFromMap(source, clz, true);
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
	}

	/**
	 * 属性拷贝，从map拷贝到clz 实例
	 * @param source
	 * @param clz
	 * @return
	 */
	public static <T> T copyFromMap(Map<String, Object> source, Class clz, boolean underscore)
			throws IllegalAccessException, InstantiationException, InvocationTargetException {
		PropertyDescriptor pdas[] = BeanUtils.getPropertyDescriptors(clz);
		Object dest = clz.newInstance();
		Iterator<Map.Entry<String, Object>> iterable = source.entrySet().iterator();
		while (iterable.hasNext()) {
			Map.Entry<String, Object> entry = iterable.next();
			for (PropertyDescriptor pd : pdas) {
				Method method = pd.getWriteMethod();
				if (method != null && method.getName().equalsIgnoreCase("set" + entry.getKey())) {
					String key = underscore ? StringUtil.underscoreName(pd.getName()) : pd.getName();
					Object value = source.get(key);
					if (value != null && method.getParameters().length == 1) {
						Type type = method.getParameters()[0].getType();
						if (type.getTypeName().equals(value.getClass().getTypeName())) {
							method.invoke(dest, value);
							//							ReflectionUtil.setFieldValue(dest,pd.getName(),value);
						}
					}
				}
			}
		}

		return (T) dest;
	}


	public static void copyProperties(Map<String, Object> source, Object target, String dateFormat,
			String... ignoreProperties) throws BeansException {

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class<?> actualEditable = target.getClass();

		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
		List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

		if (StringUtils.isEmpty(dateFormat)) {
			//Ĭ��ʱ���ʽ
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
				if (MapUtil.isNotEmpty(source)) {
					try {

						Object value = source.get(targetPd.getName());
						if (value == null) {
							continue;
						}
						if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
							writeMethod.setAccessible(true);
						}

						if (value.getClass().getTypeName().equals(writeMethod.getParameterTypes()[0].getName())) {
							//������ȫ��ͬ�����
							writeMethod.invoke(target, value);

						} else if ("java.lang.Long".equals(value.getClass().getTypeName()) && "java.util.Date".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, new Date((Long) value));
						} else if ("java.lang.Long".equals(value.getClass().getTypeName()) && "java.sql.Date".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, new java.sql.Date((Long) value));
						} else if ("java.lang.Long".equals(value.getClass().getTypeName()) && "java.sql.Timestamp".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, new Timestamp((Long) value));
						} else if ("java.lang.String".equals(value.getClass().getTypeName()) && "java.sql.Timestamp".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, new Timestamp(simpleDateFormat.parse((String) value).getTime()));
						} else if ("java.lang.String".equals(value.getClass().getTypeName()) && "java.sql.Date".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, new java.sql.Date(simpleDateFormat.parse((String) value).getTime()));
						} else if ("java.lang.String".equals(value.getClass().getTypeName()) && "java.util.Date".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, new Date(simpleDateFormat.parse((String) value).getTime()));
						} else if ("java.util.Date".equals(value.getClass().getTypeName()) && "java.lang.String".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, simpleDateFormat.format(value));
						} else if ("java.sql.Date".equals(value.getClass().getTypeName()) && "java.lang.String".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, simpleDateFormat.format(value));
						} else if ("java.sql.Timestamp".equals(value.getClass().getTypeName()) && "java.lang.String".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, simpleDateFormat.format(value));
						} else if ("java.lang.String".equals(value.getClass().getTypeName()) && "java.lang.Long".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, Long.valueOf((String) value));
						} else if ("java.lang.String".equals(value.getClass().getTypeName()) && "java.lang.Float".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, Float.valueOf((String) value));
						} else if ("java.lang.String".equals(value.getClass().getTypeName()) && "java.lang.Double".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, Double.valueOf((String) value));
						} else if ("java.lang.String".equals(value.getClass().getTypeName()) && "java.lang.Integer".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, Integer.valueOf((String) value));
						} else if ("java.lang.String".equals(value.getClass().getTypeName()) && "java.lang.Short".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, Short.valueOf((String) value));
						} else if ("java.lang.String".equals(value.getClass().getTypeName()) && "java.lang.Byte".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, Byte.valueOf((String) value));
						} else if ("java.lang.String".equals(value.getClass().getTypeName()) && "java.math.BigDecimal".equals(writeMethod.getParameterTypes()[0].getName())) {
							writeMethod.invoke(target, new BigDecimal((String) value));
						} else {
							log.error("ӳ���ֶε��������Ͳ�ƥ�䣬�޷���ӳ�䣬readMethod returnType:{},writeMethod parameterType:{}", value.getClass().getTypeName(), writeMethod.getParameterTypes()[0].getName());
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
					}
				}
			}
		}
	}

	/**
	 * 属性拷贝
	 * @param s 源对象
	 * @param d 目标对象
	 */
	public static <S, D> void copy(S s, D d) {
		BeanUtils.copyProperties(s, d);
	}

	private static Map<Class, List<Field>> fieldMap = Maps.newConcurrentMap();

	public static List<Field> getFields(Class clz) {
		if (fieldMap.containsKey(clz)) {
			return fieldMap.get(clz);
		}
		PropertyDescriptor pdas[] = BeanUtils.getPropertyDescriptors(clz);
		List<Field> fields = Lists.newArrayList();
		for (PropertyDescriptor pd : pdas) {
			if (StringUtil.equalsIgnoreCase("class", pd.getName())) {
				continue;
			}
			System.out.println(pd.getName());
			fields.add(ReflectionUtil.getField(clz, pd.getName()));
		}
		fieldMap.put(clz, fields);
		return fields;
	}
}
