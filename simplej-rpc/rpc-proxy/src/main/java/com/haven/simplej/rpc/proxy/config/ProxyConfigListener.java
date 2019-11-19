package com.haven.simplej.rpc.proxy.config;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.config.model.ConfigFileResponse;
import com.haven.simplej.rpc.config.model.ConfigItemResponse;
import com.haven.simplej.rpc.server.listener.ConfigListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: havenzhang
 * @date: 2019/10/11 0:36
 * @version 1.0
 */
@Component
@Slf4j
public class ProxyConfigListener implements ConfigListener {
	@Override
	public void configFileListen(ConfigFileResponse response) {
		log.debug("ConfigFileResponse :{}", JSON.toJSONString(response, true));
	}

	@Override
	public void propertyListen(ConfigItemResponse response) {
		log.debug("ConfigItemResponse :{}", JSON.toJSONString(response, true));

	}
}
