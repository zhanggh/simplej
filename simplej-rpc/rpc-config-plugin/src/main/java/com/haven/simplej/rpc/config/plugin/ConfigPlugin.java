package com.haven.simplej.rpc.config.plugin;

import com.haven.simplej.rpc.config.plugin.manager.ConfigManager;
import com.haven.simplej.rpc.config.service.IConfigService;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.plugin.RpcPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 配置中心插件
 * 在proxy服务启动时候进行加载，可自定义init方法
 * @author: havenzhang
 * @date: 2018/08/31 23:08
 * @version 1.0
 */
@Component
@Slf4j
public class ConfigPlugin implements RpcPlugin {


	@Autowired
	private ConfigManager configManager;

	@Override
	public void init(List<ServiceInfo> serviceInfos, Map<String, List<ServiceInstance>> methodIdInstanceMap) {
		log.debug("-------------ConfigPlugin init---------------");

		//指定当前服务的请求和响应可以在proxy做编解码
		RpcHelper.addProxyCoderNamespace(IConfigService.NAMESPACE );
	}

	@Override
	public void listenByRegister(String namespace) {
		log.debug("listenByRegister ,namespace:{}", namespace);
		configManager.listen(namespace);
	}
}
