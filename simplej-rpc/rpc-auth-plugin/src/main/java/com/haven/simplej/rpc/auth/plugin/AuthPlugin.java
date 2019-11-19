package com.haven.simplej.rpc.auth.plugin;

import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.plugin.RpcPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 权限控制插件
 * @author: havenzhang
 * @date: 2018/10/31 22:47
 * @version 1.0
 */
@Component
@Slf4j
public class AuthPlugin implements RpcPlugin {


	@Override
	public void init(List<ServiceInfo> serviceInfos, Map<String, List<ServiceInstance>> methodIdInstanceMap) {

	}

	@Override
	public void listenByRegister(String namespace) {
		log.debug("listenByRegister ,namespace:{}", namespace);
	}
}
