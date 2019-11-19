package com.haven.simplej.rpc.client.client.processor;

import com.haven.simplej.rpc.client.client.NettyClient;
import com.haven.simplej.rpc.client.client.helper.ClientHelper;
import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;
import com.haven.simplej.rpc.protocol.processor.IServiceProcessor;
import com.vip.vjtools.vjkit.logging.PerformanceUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端消息处理核心类
 * 调用的异常处理，交给了责任链进行统一捕捉管理
 * @author: havenzhang
 * @date: 2018/4/8 22:10
 * @version 1.0
 */
@Slf4j
public class ClientProcessorImpl implements IServiceProcessor {


	public ClientProcessorImpl() {
		super();
	}


	@Override
	public RpcResponse process(RpcRequest request) {
		RpcResponse response;
		PerformanceUtil.start(this.getClass().getSimpleName());
		NettyClient client = ClientHelper.initClient();
		if (request.getHeader().isSyncRequest()) {
			response = client.send(request);
		} else {
			//异步请求rpc服务
			response = client.sendAsyn(request, null);
		}
		log.debug("get response cost:{}", PerformanceUtil.end(this.getClass().getSimpleName()));
		return response;
	}
}
