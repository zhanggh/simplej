package com.haven.simplej.rpc.server.listener;

import com.haven.simplej.rpc.config.model.ConfigFileResponse;
import com.haven.simplej.rpc.config.model.ConfigItemResponse;

/**
 * 配置监听
 * @author: havenzhang
 * @date: 2018/06/11 23:13
 * @version 1.0
 */
public interface ConfigListener {

	/**
	 * 配置文件监听
	 * @param response 响应
	 */
	void configFileListen(ConfigFileResponse response);

	/**
	 * 配置属性项监听
	 * @param response 响应
	 */
	void propertyListen(ConfigItemResponse response);
}
