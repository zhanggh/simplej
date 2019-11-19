package com.haven.simplej.rpc.enums;

/**
 * rpc 服务的 角色 分类，PROXY/BUSINESS_SERVER(业务应用)/RPC_CENTER/RPC_REGISTRY、
 * 比如业务消息，交给businessServer处理
 * 服务注册消息，交给proxy处理
 * 服务治理交给rpc center
 * @author: havenzhang
 * @date: 2019/4/28 17:20
 * @version 1.0
 */
public enum RpcServerType {
	PROXY,BUSINESS_SERVER, RPC_CENTER, RPC_REGISTRY, RPC_AUTH, RPC_CONFIG, RPC_MOCK
}
