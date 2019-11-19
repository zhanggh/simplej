package com.haven.simplej.rule.engine.plugin;

import com.haven.simplej.rule.engine.emuns.RuleError;
import com.haven.simplej.rule.engine.exception.RuleException;
import com.haven.simplej.script.ExpressionParserUtil;
import com.haven.simplej.script.groovy.GroovyFactory;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import groovy.lang.GroovyObject;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 插件执行器
 * @author: havenzhang
 * @date: 2019/4/18 21:58
 * @version 1.0
 */
public class OperatorPluginExecutor {

	/**
	 * 执行java代码操作符
	 * @param pluginCode java code
	 * @param params 参数
	 * @return 返回值
	 */
	public static <T> T executeUtil(String pluginCode, List params) {
		T result = null;
		try {
			String[] strings = pluginCode.split("#");
			Class<?> threadClazz = Class.forName(strings[0]);
			Method method;
			if (CollectionUtil.isNotEmpty(params)) {
				Class<?>[] paramTypes = new Class[params.size()];
				for (int i = 0; i < params.size(); i++) {
					paramTypes[i] = params.get(i).getClass();
				}
				method = threadClazz.getMethod(strings[1], paramTypes);
			} else {
				method = threadClazz.getMethod(strings[1]);
			}
			result = (T) method.invoke(null, params.toArray());
		} catch (Exception e) {
			throw new RuleException(e, RuleError.java_plugin_parser_error);
		}
		return result;
	}


	/**
	 * 执行el表达式
	 * @param elCode  el 表达式
	 * @param params 参数
	 * @return 返回值
	 */
	public static <T> T executeEl(String elCode, Map<String, Object> params, Class returnType) {
		T result = null;
		try {
			result = (T) ExpressionParserUtil.parser(elCode, params, returnType);
		} catch (Exception e) {
			throw new RuleException(e, RuleError.el_plugin_parser_error);
		}
		return result;
	}

	/**
	 * 执行el表达式
	 * @param groovyCode  el 表达式
	 * @param params 参数
	 * @return 返回值
	 */
	public static <T> T executeGroovy(long pluginId, String groovyCode, Map<String, Object> params) {
		T result = null;
		String methodName = "execute";
		try {
			Class clz = GroovyFactory.getFactory().getGroovyClass(groovyCode, String.valueOf(pluginId));
			GroovyObject object  = (GroovyObject) clz.newInstance();
			Method method = clz.getMethod(methodName, Map.class);
			result = (T) method.invoke(object, params);
		} catch (Exception e) {
			throw new RuleException(e, RuleError.groovy_plugin_parser_error);
		}
		return result;
	}
}
