package com.haven.simplej.rpc.proxy.helper;

import com.google.common.collect.Sets;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.constant.HttpField;
import com.haven.simplej.rpc.proxy.http.model.HttpRequest;
import com.haven.simplej.text.StringUtil;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * 代理帮助类
 * @author: havenzhang
 * @date: 2018/9/13 14:01
 * @version 1.0
 */
public class ProxyHelper {

	/**
	 * 注册中心提供的方法
	 */
	private static Set<String> registerMethodIds = Sets.newHashSet();
	/**
	 * 注册中心提供的服务
	 */
	private static Set<String> registerServices = Sets.newHashSet();

	/**
	 * proxy守护的业务app 命名空间，
	 * 也就是某个业务app通过该proxy服务发起服务服务注册时，
	 * 代表该app是proxy的守护app服务
	 */
	private static Set<String> namespaceSet = Sets.newHashSet();

	/**
	 * 获取RPCHeader
	 * @param httpReq
	 * @return
	 */
	public static RpcHeader getRpcHeader(HttpRequest httpReq) {
		Map<String, String> httpHeader = httpReq.getHttpHeader();
		RpcHeader header = new RpcHeader();
		header.setVersion(httpHeader.getOrDefault(HttpField.version, RpcConstants.VERSION_1));
		header.setNamespace(httpHeader.get(HttpField.namespace));
		header.setServerVersion(httpHeader.get(HttpField.serviceVersion));
		header.setServiceName(httpHeader.get(HttpField.serviceName));
		header.setMethodName(httpHeader.get(HttpField.methodName));
		header.setMethodParamTypes(httpHeader.get(HttpField.methodParamTypes));
		//非强制字段
		header.setMethodReturnType(httpHeader.get(HttpField.methodReturnType));
		header.setMethodId(httpHeader.get(HttpField.methodId));
		httpHeader.forEach((k, v) -> {
			if (!headerFields.contains(k)) {
				header.getAddition().put(k, v);
			}
		});
		return header;
	}

	private static Set<String> headerFields = Sets.newHashSet();

	static {
		headerFields.add(HttpField.namespace);
		headerFields.add(HttpField.serviceName);
		headerFields.add(HttpField.serviceVersion);
		headerFields.add(HttpField.methodName);
		headerFields.add(HttpField.methodId);
		headerFields.add(HttpField.methodReturnType);
		headerFields.add(HttpField.methodParamTypes);
		headerFields.add(HttpField.version);
	}


	/**
	 * 判断是否为rpc请求
	 * @param header http头
	 * @return
	 */
	public static boolean toRpcServer(Map<String, String> header) {
		String methodId = header.get(HttpField.methodId);
		String serviceName = header.get(HttpField.serviceName);

		String methodName = header.get(HttpField.methodName);
		if (StringUtil.isNotEmpty(methodId) || (StringUtil.isNotEmpty(serviceName) || StringUtil.isNotEmpty(methodName))) {
			return true;
		}
		return false;
	}

	static {

	}


	public static void addRegisterMethod(String methodId) {
		registerMethodIds.add(methodId);
	}

	public static void addRegisterService(String serviceName) {
		registerServices.add(serviceName);
	}

	/**
	 * 增加注册中心接口方法信息到本地内存缓存中
	 * @param clz 接口类
	 */
	public static void addRegisterMethod(Class clz) {
		Method[] methods = clz.getMethods();
		for (Method method : methods) {
			String typeString = RpcHelper.getParamTypesStr(method.getParameterTypes());
			String methodIdKey = RpcHelper.getMethodId(clz.getName(), method.getName(), typeString);
			addRegisterMethod(methodIdKey);
			RpcHelper.addMethod(methodIdKey, method);
			addRegisterService(clz.getName());
		}
	}

	/**
	 * 判断请求的远程方法是否为注册中心提供的服务
	 * @param methodId 远程方法的唯一id
	 * @return boolean
	 */
	public static boolean isRegisterMethod(String methodId) {

		return registerMethodIds.contains(methodId);
	}

	/**
	 * 判断请求的远程方法是否为注册中心提供的服务
	 * @param serviceName 远程服务名
	 * @return boolean
	 */
	public static boolean isRegisterService(String serviceName) {

		return registerServices.contains(serviceName);
	}


	/**
	 * 往缓存中增加命名空间
	 * @param namespace 命名空间
	 */
	public static void addAppNamespace(String namespace){
		namespaceSet.add(namespace);
	}

	/**
	 * 获取当前proxy服务的所有守护app服务的命名空间
	 * @return Set
	 */
	public static Set<String> getNamespaceSet(){
		return namespaceSet;
	}
}
