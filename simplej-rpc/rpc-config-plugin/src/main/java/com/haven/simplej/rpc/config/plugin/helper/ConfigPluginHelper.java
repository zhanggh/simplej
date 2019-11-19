package com.haven.simplej.rpc.config.plugin.helper;

import com.google.common.collect.Maps;
import com.haven.simplej.rpc.config.service.IConfigService;
import com.haven.simplej.rpc.helper.RpcHelper;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author: havenzhang
 * @date: 2018/11/3 19:23
 * @version 1.0
 */
public class ConfigPluginHelper {


	/**
	 * configService类的方法id信息
	 */
	private static Map<String, Method> methodMap = Maps.newConcurrentMap();


	static {
		Method[] methods = IConfigService.class.getMethods();
		for (Method method : methods) {
			String methodId = RpcHelper.getMethodId(IConfigService.class.getName(), method);
			methodMap.put(methodId, method);
		}

	}

	public static boolean isConfigMethod(String methodId) {
		return methodMap.containsKey(methodId);
	}


	public static Method getMethod(String methodId) {
		return methodMap.get(methodId);
	}
}
