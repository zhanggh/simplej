package com.haven.simplej.rpc.proxy.http.dispatch;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.constant.HttpField;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.enums.SerialType;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.rpc.filter.FilterChain;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.filter.impl.RpcFilterChain;
import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;
import com.haven.simplej.rpc.proxy.enums.ContentType;
import com.haven.simplej.rpc.proxy.helper.ProxyHelper;
import com.haven.simplej.rpc.proxy.http.model.HttpRequest;
import com.haven.simplej.rpc.proxy.http.model.HttpResponse;
import com.haven.simplej.rpc.proxy.processor.HttpProxyProcessorImpl;
import com.haven.simplej.rpc.proxy.processor.TcpProxyProcessorImpl;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.base.ExceptionUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * http转发器，转发到 http服务或者rpc服务
 * @author: havenzhang
 * @date: 2018/5/15 22:47
 * @version 1.0
 */
@Data
@Component
@Slf4j
public class HttpDispatcherImpl implements HttpDispatcher {

	/**
	 * 过滤器
	 */
	@Autowired
	private List<RpcFilter> filters;
	@Autowired
	private TcpProxyProcessorImpl tcpProcessor;
	@Autowired
	private HttpProxyProcessorImpl httpProcessor;


	@Override
	public HttpResponse dispatch(HttpRequest httpRequest) {
		HttpResponse httpResponse = new HttpResponse();
		httpResponse.setHeader(httpRequest.getHttpHeader());
		RpcRequest request = new RpcRequest();
		request.setBody(request);
		RpcResponse response = new RpcResponse();
		try {
			Map<String, String> header = httpRequest.getHttpHeader();
			String encoding = header.getOrDefault(HttpField.encoding, RpcConstants.DEFAULT_ENCODE);
			String namespace = header.get(HttpField.namespace);
			String serviceVersion = header.get(HttpField.serviceVersion);
			if (StringUtil.isEmpty(serviceVersion)) {
				httpResponse.setBody("serviceVersion can not be empty,add header".getBytes(encoding));
				return httpResponse;
			}
			if (StringUtil.isEmpty(namespace)) {
				httpResponse.setBody("namespace can not be empty,add header".getBytes(encoding));
				return httpResponse;
			}

			String contentType = header.get(HttpField.contentType);
			FilterChain chain = new RpcFilterChain();
			if (StringUtil.equalsIgnoreCase(ContentType.APPLICATION_JSON.getValue(), contentType) && ProxyHelper.toRpcServer(header)) {
				//http参数转换成rpc参数，转发到tcp服务
				convert2RpcRequest(request, httpRequest);
				((RpcFilterChain) chain).init(getTcpProcessor(), getFilters());
				chain.doFilter(request, response);
			} else {
				//转发到web服务
				((RpcFilterChain) chain).init(getHttpProcessor(), getFilters());
				chain.doFilter(httpRequest, response);
			}

			//处理响应结果
			httpResponse.setBody(JSON.toJSONBytes(response.getBody()));
			response.getHeader().getAddition().forEach((k, v) -> httpResponse.getHeader().put(k, v));
			httpResponse.getHeader().put(HttpField.httpRespCode, RpcError.SUCCESS.getErrorCode());
			httpResponse.getHeader().put(HttpField.httpRespMsg, RpcError.SUCCESS.getErrorMsg());
		} catch (Exception e) {
			log.error("dispatch error", e);
			String error;
			if (e instanceof RpcException) {
				RpcException rpcException = (RpcException) e;
				httpResponse.getHeader().put(HttpField.httpRespCode, rpcException.getRespCode());
				httpResponse.getHeader().put(HttpField.httpRespMsg, rpcException.getRespMsg());
				error = rpcException.getRespMsg();
			} else {
				error = ExceptionUtil.stackTraceText(e);
				httpResponse.getHeader().put(HttpField.httpRespCode, RpcError.SYSTEM_BUSY.getErrorCode());
				httpResponse.getHeader().put(HttpField.httpRespMsg, error);
			}
			httpResponse.setBody(error.getBytes());
		}
		return httpResponse;
	}

	/**
	 * tcp 请求参数校验
	 * @param request
	 */
	private void tcpParamCheck(RpcRequest request) {

		if (StringUtil.isNotEmpty(request.getHeader().getMethodId())) {
			return;
		}
		if (StringUtil.isEmpty(request.getHeader().getMethodName())) {
			throw new RpcException(RpcError.RPC_FIELD_ERROR, "methodName can not be empty");
		}
		if (StringUtil.isEmpty(request.getHeader().getServerVersion())) {
			throw new RpcException(RpcError.RPC_FIELD_ERROR, "serviceVersion can not be empty");
		}
		if (StringUtil.isEmpty(request.getHeader().getServiceName())) {
			throw new RpcException(RpcError.RPC_FIELD_ERROR, "serviceName can not be empty");
		}
		if (StringUtil.isEmpty(request.getHeader().getMethodParamTypes())) {
			throw new RpcException(RpcError.RPC_FIELD_ERROR, "methodParamTypes can not be empty");
		}

	}

	/**
	 * 将http请求对象转换成标准的rpc请求
	 * @param request rpcRequest
	 * @param httpRequest httpRequest
	 */
	private void convert2RpcRequest(RpcRequest request, HttpRequest httpRequest) {
		RpcHeader header = ProxyHelper.getRpcHeader(httpRequest);
		//请求参数校验
		tcpParamCheck(request);

		//如果http contentType 是 application/json的时候
		header.setSerialType(SerialType.JSON.getValue());
		//json 报文
		request.setBody(httpRequest.getBody());
		request.setHeader(header);
	}

}
