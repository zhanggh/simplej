package com.haven.simplej.rpc.client.client.helper;

import com.google.common.collect.Maps;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.client.client.NettyClient;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.model.Server;
import com.haven.simplej.rpc.protocol.context.InvocationContext;
import com.haven.simplej.text.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 客户端帮助类
 * @author: havenzhang
 * @date: 2019/1/10 10:54
 * @version 1.0
 */
@Slf4j
public class ClientHelper {

	/**
	 * 默认的proxy ip地址
	 */
	private static final String DEFALT_PROXY_HOST = "127.0.0.1";

	/**
	 * 客户端缓存
	 */
	private static Map<String, NettyClient> clientMap = Maps.newConcurrentMap();


	/**
	 * 初始化客户端
	 */
	public static synchronized NettyClient initClient() {

		//如果没有设置静态路由，则走proxy
		//如果有配置本地代理，优先走代理，
		String host = PropertyManager.get(RpcConstants.RPC_PROXY_HOST_KEY, DEFALT_PROXY_HOST);
		String port = PropertyManager.get(RpcConstants.RPC_PROXY_PORT_KEY);

		if (StringUtil.isEmpty(host)||StringUtil.isEmpty(port)) {
			//静态路由（直接访问业务app服务的模式），没有配置本地代理则走静态路由
			host = PropertyManager.get(RpcConstants.RPC_APP_SERVER_HOST_KEY);
			port = PropertyManager.get(RpcConstants.RPC_APP_SERVER_PORT_KEY);
		}
		//优先从当前线程取目的地址
		Server server = InvocationContext.getServer();
		if (server != null) {
			host = server.getHost();
			port = String.valueOf(server.getPort());
		}
		log.debug("init ClientProcessorImpl host:{} port:{}", host, port);
		if (StringUtil.isEmpty(host)||StringUtil.isEmpty(port)) {
			String errorMsg = "rpc.proxy.port not set,please set it in properties file or System.setProperties";
			log.error(errorMsg);
			throw new UncheckedException(errorMsg);
		}

		String key = host + ":" + port;
		NettyClient client = clientMap.get(key);
		if (client == null) {
			client = NettyClient.getInstance(host, Integer.parseInt(port));
			clientMap.put(key, client);
		}
		return client;
	}
}
