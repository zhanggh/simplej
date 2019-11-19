package com.haven.simplej.rpc.config.plugin.service;

import com.haven.simplej.rpc.config.model.ConfigFileResponse;
import com.haven.simplej.rpc.config.model.ConfigFileRpcModel;
import com.haven.simplej.rpc.config.model.ConfigItemResponse;
import com.haven.simplej.rpc.config.model.ConfigItemRpcModel;
import com.haven.simplej.rpc.config.service.IConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: havenzhang
 * @date: 2018/11/3 22:53
 * @version 1.0
 */
@Component
@Slf4j
public class ConfigPluginServiceImpl implements IConfigService {
	@Override
	public ConfigFileResponse queryNewConfigFile(String namespace, boolean waitForChange) {
		return null;
	}

	@Override
	public ConfigItemResponse queryNewConfigItem(String namespace, boolean waitForChange) {
		return null;
	}

	@Override
	public int addConfigFile(ConfigFileRpcModel configFile) {
		return 0;
	}

	@Override
	public int updateConfigFile(ConfigFileRpcModel configFile) {
		return 0;
	}

	@Override
	public int addConfigItem(ConfigItemRpcModel configItem) {
		return 10;
	}

	@Override
	public int updateConfigItem(ConfigItemRpcModel configItem) {
		return 0;
	}

	@Override
	public ConfigItemRpcModel getConfigItem(String namespace, String key) {
		return null;
	}

	@Override
	public ConfigItemRpcModel getConfigFile(String namespace, String fileName) {
		return null;
	}
}
