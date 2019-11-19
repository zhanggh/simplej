package com.haven.simplej.rpc.mock.plugin.filter;

import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.enums.FilterOrder;
import com.haven.simplej.rpc.enums.SerialType;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.rpc.filter.FilterChain;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.mock.model.MockMethod;
import com.haven.simplej.rpc.mock.plugin.MockPlugin;
import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.protocol.response.ResponseHelper;
import com.haven.simplej.text.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @author: havenzhang
 * @date: 2018/10/31 23:44
 * @version 1.0
 */
@Component
@Slf4j
public class MockFilter implements RpcFilter {

	@Autowired
	private MockPlugin plugin;

	@Override
	public void doFilter(RpcRequest request, RpcResponse response, FilterChain chain) {

		log.debug("--------------------------------mock filter--------------------------------");
		RpcHeader header = request.getHeader();
		MockMethod methodInfo;
		if (StringUtil.isNotEmpty(header.getMethodId())) {
			methodInfo = plugin.getMethodInfo(header.getMethodId(), header.getServerVersion());
		} else {
			methodInfo = plugin.getMethodInfo(header.getServiceName(), header.getMethodName(),
					header.getServerVersion(), header.getMethodParamTypes());
		}

		//判断是否需要对该请求进行mock处理
		if (isMock(methodInfo, header)) {
			try {
				//模拟挡板的时候,直接把json模拟数据返回给客户端，由客户端来做反序列化
				request.getHeader().setMockResponse(true);
				//如果是mock的时候，序列化方式必须是json
				request.getHeader().setSerialType(SerialType.JSON.getValue());
				ResponseHelper.setResponse(methodInfo.getResponse().getBytes(RpcConstants.DEFAULT_ENCODE),
						request.getHeader(), response);
				log.debug("response from mock data:", methodInfo.getResponse());
			} catch (UnsupportedEncodingException e) {
				log.error("UnsupportedEncodingException", e);
				throw new RpcException(RpcError.SYSTEM_BUSY);
			}
			return;
		}
		//继续下一个filter
		chain.doFilter(request, response);
	}


	/**
	 * 是否进行mock
	 * @param methodInfo mock方法信息
	 * @return boolean
	 */
	private boolean isMock(MockMethod methodInfo, RpcHeader header) {

		String serverHost = header.getServerIp();
		String serverPort = header.getServerPort();
		String key = getMockInstanceKey(serverHost, serverPort);
		boolean mock = false;
		if (methodInfo != null) {
			if (StringUtil.isEmpty(serverHost)) {
				//如果客户端不指定服务端地址
				mock = true;
			} else {
				for (ServiceInstance instance : methodInfo.getInstances()) {
					if (StringUtil.equalsIgnoreCase(getMockInstanceKey(instance.getHost(),
							String.valueOf(instance.getPort())), key)) {
						mock = true;
						break;
					}
				}
			}
		}
		return mock;
	}

	private String getMockInstanceKey(String serverHost, String serverPort) {

		return serverHost + ":" + serverPort;
	}

	@Override
	public int getOrder() {
		return FilterOrder.MOCK_FILTER.order();
	}
}
