package com.haven.simplej.rpc.proxy.constant;

/**
 * 常量
 * @author: havenzhang
 * @date: 2018/6/9 21:50
 * @version 1.0
 */
public class RpcProxyConstant {

	/**
	 * 流量路由策略
	 */
	public static final String RPC_PROXY_ROUTE_STRATEGY_KEY="rpc.proxy.route.strategy";

	/**
	 * proxy监听服务列表的时间间隔
	 */
	public static final String RPC_PROXY_SERVICE_LISTEN_TIME_GAP_KEY="rpc.proxy.service.listen.time.gap";

	/**
	 * 是否定时获取服务列表
	 */
	public static final String RPC_PROXY_SERVICE_LISTEN_SCHEDULE="rpc.proxy.service.listen.schedule";


	/**
	 * 注册中的服务列表，为什么要指定注册中心的服务列表？因为注册中心的服务是没有服务上报和监听的
	 * （难不成注册中心自己向自己注册？）
	 * 例如：com.haven.simplej.rpc.register.service.IServiceRegister;com.haven.simplej.rpc.register.service.xxxx;
	 * 多个以分号分隔
	 */
	public static final String RPC_REGISTER_INTERFACE_CLASS_NAMES="rpc.register.interface.class.names";
	/**
	 * 服务列表同步之后更新本地内存的超时时间
	 */
	public static final String RPC_REGISTER_SERVICE_LIST_SYN_UPDATE_TIMEOUT="rpc.register.service.list.update.timeout";

}
