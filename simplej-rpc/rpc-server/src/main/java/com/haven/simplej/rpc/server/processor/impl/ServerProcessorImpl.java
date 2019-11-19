package com.haven.simplej.rpc.server.processor.impl;

import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;
import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceLookupKey;
import com.haven.simplej.rpc.protocol.context.InvocationContext;
import com.haven.simplej.rpc.protocol.processor.IServiceProcessor;
import com.haven.simplej.rpc.server.processor.BaseProcessor;
import com.vip.vjtools.vjkit.logging.PerformanceUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * APP服务端业务模块的rpc请求处理器
 * 调用的异常处理，交给了责任链进行统一捕捉管理
 * @author: havenzhang
 * @date: 2018/4/28 18:29
 * @version 1.0
 */
@Slf4j
public class ServerProcessorImpl extends BaseProcessor implements IServiceProcessor {
	@Override
	public RpcResponse process(RpcRequest request) {
		PerformanceUtil.start();
		RpcResponse response;

		if (request.getHeader() != null) {
			InvocationContext.setTraceId(request.getHeader().getMsgId());
		}
		//获取service 查找key
		ServiceLookupKey lookupKey = getLookupKey(request.getHeader());
		//从本地缓存中查询服务信息
		ServiceInfo serviceInfo = getServiceInfo(lookupKey);
		//执行本地的service方法
		response = invokeLocalMethod(serviceInfo, request);

		log.debug("process cost:{}", PerformanceUtil.end());
		return response;
	}


}
