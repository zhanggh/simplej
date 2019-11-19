package com.haven.simplej.rpc.proxy.plugin;

import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.plugin.RpcPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: havenzhang
 * @date: 2018/11/3 15:57
 * @version 1.0
 */
@Slf4j
@Component
public class ProxyPlugin implements RpcPlugin {
	@Override
	public void init(List<ServiceInfo> serviceInfos, Map<String, List<ServiceInstance>> methodIdInstanceMap) {

	}

	@Override
	public void listenByRegister(String namespace) {

	}
}
