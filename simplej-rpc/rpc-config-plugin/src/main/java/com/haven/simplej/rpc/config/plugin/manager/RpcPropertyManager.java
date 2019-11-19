package com.haven.simplej.rpc.config.plugin.manager;

import com.google.common.collect.Maps;
import com.haven.simplej.rpc.config.model.ConfigFileResponse;
import com.haven.simplej.rpc.config.model.ConfigFileRpcModel;
import com.haven.simplej.rpc.config.model.ConfigItemResponse;
import com.haven.simplej.rpc.config.model.ConfigItemRpcModel;

import java.util.Map;
import java.util.Properties;

/**
 * 配置中心配置管理器
 * @author: havenzhang
 * @date: 2018/11/3 16:09
 * @version 1.0
 */
public class RpcPropertyManager {

	/**
	 * key是namespace
	 * 保存了对应的namespace下的所有properties属性
	 */
	private static Map<String, Properties> propertiesMap = Maps.newConcurrentMap();
	private static Map<String, ConfigItemResponse> itemResponseMap = Maps.newConcurrentMap();
	private static Map<String, ConfigFileResponse> fileResponseMap = Maps.newConcurrentMap();
	private static Map<String, ConfigItemRpcModel> itemMap = Maps.newConcurrentMap();
	private static Map<String, ConfigFileRpcModel> fileMap = Maps.newConcurrentMap();


	public static void addConfigItemResponse(String namespace, ConfigItemResponse response) {

		itemResponseMap.put(namespace, response);
	}

	public static ConfigItemResponse getItemResponse(String namespace) {

		return itemResponseMap.get(namespace);
	}

	public static ConfigFileResponse getFileResponse(String namespace) {

		return fileResponseMap.get(namespace);
	}

	public static void addConfigItem(String namespace, String key, ConfigItemRpcModel itemRpcModel) {

		itemMap.put(buildKey(namespace,key), itemRpcModel);
	}

	public static void addConfigFile(String namespace, String fileName, ConfigFileRpcModel fileRpcModel) {

		fileMap.put(buildKey(namespace,fileName), fileRpcModel);
	}

	public static ConfigItemRpcModel getItemModel(String namespace, String key) {
		return itemMap.get(buildKey(namespace,key));
	}

	public static ConfigFileRpcModel getFileModel(String namespace, String fileName) {
		return fileMap.get(buildKey(namespace,fileName));
	}

	/**
	 * 保存属性配置信息
	 * @param namespace 服务命名空间
	 * @param key 属性key
	 * @param value 属性值
	 */
	public static void addItem(String namespace, String key, String value) {

		if (!propertiesMap.containsKey(namespace)) {
			synchronized (namespace.intern()) {
				if (!propertiesMap.containsKey(namespace)) {
					Properties prop = new Properties();
					propertiesMap.put(namespace, prop);
				}
			}
		}
		propertiesMap.get(namespace).setProperty(key, value);
	}

	/**
	 * 获取某个域（服务）下的所有属性信息
	 * @param namespace 服务命名空间
	 * @return Properties
	 */
	public static Properties getProp(String namespace) {
		return propertiesMap.get(namespace);
	}

	/**
	 * 获取属性
	 * @param namespace 服务命名空间
	 * @param key 属性key
	 * @return 属性值
	 */
	public static String get(String namespace, String key) {
		if (!propertiesMap.containsKey(key)) {
			return null;
		}
		return (String) propertiesMap.get(namespace).get(key);
	}

	private static String buildKey(String namespace, String key) {

		return namespace + ":" + key;
	}
}
