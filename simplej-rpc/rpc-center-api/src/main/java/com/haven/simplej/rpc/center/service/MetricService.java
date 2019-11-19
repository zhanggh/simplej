package com.haven.simplej.rpc.center.service;

import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcMethod;
import com.haven.simplej.rpc.annotation.RpcService;
import com.haven.simplej.rpc.annotation.RpcStruct;
import com.haven.simplej.rpc.center.model.SqlLogModel;
import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;

/**
 * 流量测量服务，包括rpc请求的流量上报，SQL执行的结果上报
 * @author: havenzhang
 * @date: 2018/9/28 16:34
 * @version 1.0
 */
@RpcService(timeout = 200)
@Doc(value = "流量监控",author = "havenzhang",date = "20180303")
public interface MetricService  extends BaseRpcService{

	/**
	 * 流量上报至治理中心
	 * 统一由治理中心进行流量分析
	 * @param request 请求对象
	 * @param response 响应对象
	 */
	@RpcMethod(timeout = 500)
	@Doc(value = "流量上报至治理中心,统一由治理中心进行流量分析")
	void reportRpcCall(@RpcStruct RpcRequest request, @RpcStruct RpcResponse response);


	@RpcMethod(timeout = 500)
	@Doc(value = "SQL执行结果上报至治理中心,统一由治理中心进行SQL执行效率分析")
	void reportSqlCall(@RpcStruct SqlLogModel sqlCallModel);
}
