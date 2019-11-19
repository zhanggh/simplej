package com.haven.simplej.rpc.server.processor;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.client.client.NettyClient;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.*;
import com.haven.simplej.rpc.protocol.context.InvocationContext;
import com.haven.simplej.rpc.protocol.response.ResponseHelper;
import com.haven.simplej.rpc.server.helper.ServiceInfoHelper;
import com.haven.simplej.rpc.server.loader.ServiceLoader;
import com.haven.simplej.spring.SpringContext;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.base.type.Pair;
import com.vip.vjtools.vjkit.net.NetUtil;
import com.vip.vjtools.vjkit.reflect.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * rpc请求执行器，接收到客户端的远程调用请求时，由此类进行本地调用
 * @author: havenzhang
 * @date: 2018/5/7 21:46
 * @version 1.0
 */
@Slf4j
public class BaseProcessor {


	/**
	 * 获取service信息
	 * @param lookupKey 查找key
	 * @return ServiceInfo
	 */
	public ServiceInfo getServiceInfo(ServiceLookupKey lookupKey) {
		ServiceInfo serviceInfo = ServiceLoader.getInstance().getLocalService(lookupKey);
		if (serviceInfo == null) {
			lookupKey.setNamespace(null);
			serviceInfo = ServiceLoader.getInstance().getLocalService(lookupKey);
		}
		return serviceInfo;
	}

	/**
	 * 获取service 查找的key
	 * @param header 请求报文头
	 * @return ServiceLookupKey
	 */
	protected ServiceLookupKey getLookupKey(RpcHeader header) {
		ServiceLookupKey lookupKey = new ServiceLookupKey();
		lookupKey.setServiceName(header.getServiceName());
		lookupKey.setVersion(header.getVersion());
		lookupKey.setNamespace(header.getNamespace());
		return lookupKey;
	}


	protected RpcResponse buildResponse(RpcRequest request) {
		RpcResponse response = new RpcResponse();
		RpcHeader header = new RpcHeader();
		BeanUtils.copyProperties(request.getHeader(), header);
		header.setRespCode(RpcError.SUCCESS.getErrorCode());
		header.setRespMsg(RpcError.SUCCESS.getErrorMsg());
		response.setHeader(header);
		return response;
	}


	/**
	 * 调用本地方法
	 * @param serviceInfo 服务信息
	 * @param request 请求信息
	 * @return RpcResponse
	 */
	public RpcResponse invokeLocalMethod(ServiceInfo serviceInfo, RpcRequest request) {
		RpcResponse response = buildResponse(request);
		Class clz;
		Method method = null;
		RpcHeader header = request.getHeader();
		Pair<Class, Method> pair;
		if (StringUtil.isNotEmpty(header.getMethodId())) {
			pair = ServiceInfoHelper.getServiceAndMethod(header.getMethodId());
			if (pair == null) {
				response = ResponseHelper.buildResponse(RpcError.ERROR_METHOD_ID, header.getServiceName() + " of " +
						"class can not be found ", request.getHeader());
				return response;
			}
			clz = pair.getLeft();
			method = pair.getRight();
			if (StringUtil.isEmpty(header.getMethodParamTypes())) {
				header.setMethodParamTypes(RpcHelper.getParamTypesStr(method.getParameterTypes()));
			}
			header.setMethodName(method.getName());
			if (StringUtil.isEmpty(header.getServiceName())) {
				Class serviceClz = ServiceInfoHelper.getServiceClass(header.getMethodId());
				header.setServiceName(serviceClz.getName());
			}
		} else {
			clz = ServiceInfoHelper.getServiceClsss(serviceInfo.getServiceName());
		}
		if (clz == null) {
			response = ResponseHelper.buildResponse(RpcError.SERVICE_CLASS_NOT_FOUND, serviceInfo.getServiceName() +
					" of class can not be found ", request.getHeader());
			return response;
		}

		//根据class 类获取对应的service实现类的实例
		Object service = SpringContext.getBean(clz);
		if (service == null) {
			String error = "can not find service instance of class :" + clz.getName();
			return ResponseHelper.buildResponse(RpcError.SERVICE_NOT_FOUND, error, request.getHeader());
		}

		Object respBody;
		InvocationContext.setHeader(header);
		//调用方法
		String paramsTypes = StringUtil.trimToEmpty(header.getMethodParamTypes());
		if (method == null) {
			method = ServiceInfoHelper.getMethod(header.getServiceName(), header.getMethodName(), paramsTypes);
		}
		if (method == null) {
			return ResponseHelper.buildResponse(RpcError.METHOD_NOT_FOUND, "method can not be null",
					request.getHeader());
		}
		//获取请求参数集
		List list = (List) request.getBody();

		//执行本地方法调用
		if (request.isFileTransfer()) {
			//文件传输的时候，方法只能有一个参数，并且是byte[] 数组
			respBody = ReflectionUtil.invokeMethod(service, method, new Object[]{request.getBytesArray()});
		} else {
			respBody = ReflectionUtil.invokeMethod(service, method, list.toArray());
		}
		response.setBody(respBody);
		if (respBody instanceof List || respBody instanceof ArrayList || respBody instanceof LinkedList) {
			response.getHeader().setMethodReturnType(List.class.getName());
			packList((List) respBody, response);
		}

		return response;
	}

	/**
	 * 针对list集合的序列化做特殊处理，因为Protostuff的特殊性，具体看ProtostuffUtil.java
	 * @param list 集合
	 * @param response 响应对象
	 */
	private void packList(List list, RpcResponse response) {
		RpcBody<List> body = new RpcBody<>();
		body.setBody(list);
		response.setBody(body);
	}


	/**
	 * 发送rpc请求
	 * @param instance 实例信息
	 * @param request 请求模型
	 * @return RpcResponse
	 */
	protected RpcResponse send(ServiceInstance instance, RpcRequest request) {
		//如果还是找不到，那么就抛异常了
		if (instance == null) {
			log.debug("request header:{}", JSON.toJSONString(request.getHeader(), true));
			return ResponseHelper.buildResponse(RpcError.SERVICE_INSTANCE_NOT_FOUND, "instance can not be found " +
					"service ", request.getHeader());
		}
		int port = instance.getProxyPort();
		//网格架构，app--》proxy--》proxy --》app
		boolean proxyToProxy = PropertyManager.getBoolean(RpcConstants.RPC_PROXY_TO_PROXY_KEY, false);
		if (instance.isLocal() || !proxyToProxy || port == 0 || StringUtil.equals(RpcConstants.LOCAHOST,
				instance.getHost()) || StringUtil.equals(NetUtil.getLocalHost(), instance.getHost())) {
			port = instance.getPort();
		}
		//自动补上服务ip和端口
		request.getHeader().setServerIp(instance.getHost());
		request.getHeader().setServerPort(String.valueOf(port));
		//远程调用
		NettyClient client = NettyClient.getInstance(instance.getHost(), port);
		log.debug("dispatch request to service instance：{}", JSON.toJSONString(instance, true));
		RpcResponse response = client.send(request);
		return response;
	}
}
