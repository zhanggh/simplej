package com.haven.simplej.rpc.client.client.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.net.NetUtil;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.annotation.RpcMethod;
import com.haven.simplej.rpc.annotation.RpcService;
import com.haven.simplej.rpc.callback.Fallback;
import com.haven.simplej.rpc.client.client.callback.IServiceCallBack;
import com.haven.simplej.rpc.client.client.message.MessageHelper;
import com.haven.simplej.rpc.client.client.threadpool.ClientThreadPoolFactory;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.enums.MsgFlag;
import com.haven.simplej.rpc.enums.RequestMode;
import com.haven.simplej.rpc.enums.RpcServerType;
import com.haven.simplej.rpc.enums.SerialType;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.rpc.filter.FilterChain;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.filter.impl.RpcFilterChain;
import com.haven.simplej.rpc.filter.impl.RpcLogFilter;
import com.haven.simplej.rpc.filter.impl.RpcParamValidateFilter;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.*;
import com.haven.simplej.rpc.protocol.context.InvocationContext;
import com.haven.simplej.rpc.protocol.processor.IServiceProcessor;
import com.haven.simplej.rpc.util.ReflectUtil;
import com.haven.simplej.security.enums.SignAlgorithm;
import com.haven.simplej.spring.SpringContext;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.logging.PerformanceUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 服务接口代理
 * @author: havenzhang
 * @date: 2018/01/28 21:22
 * @version 1.0
 */
@Slf4j
@Data
public class ServiceProxy implements InvocationHandler {


	/**
	 * 过滤器
	 */
	private List<RpcFilter> filters;

	/**
	 * 业务处理器
	 */
	private IServiceProcessor processor;


	/**
	 * 方法参数类型缓存
	 */
	private static final Map<Method, String> methodTypesMap = Maps.newConcurrentMap();

	/**
	 * service目标服务的namespace缓存
	 */
	private static final Map<Class, String> namespaceMap = Maps.newConcurrentMap();
	/**
	 * 业务回调,每个方法对应一个回调，key格式：service class name +"@" + method.getName()
	 */
	private Map<String, IServiceCallBack> iServiceCallBackMap = Maps.newHashMap();

	/**
	 * 快速回退的方法钩子，用于快速失败的时候，执行预先设定的钩子
	 */
	private Map<String, Fallback> fallbackMap = Maps.newHashMap();

	/**
	 * 接口代理实例
	 */
	private Object interfaceService;

	/**
	 * 构建参数
	 */
	private Builder builder;


	public ServiceProxy(Builder builder) {
		this.builder = builder;
		this.processor = builder.getProcessor();
		this.filters = builder.getFilters();
		if (SpringContext.getContext() != null) {
			List<RpcFilter> filterList = SpringContext.getBeansOfType(RpcFilter.class);
			if (CollectionUtil.isNotEmpty(filterList)) {
				this.filters.addAll(filterList);
			}
		}
		//添加默认的过滤器
		addDefaultFilter(this.filters);
		//异步回调
		if (CollectionUtil.isNotEmpty(builder.getCallBackList())) {
			for (IServiceCallBack iServiceCallBack : builder.getCallBackList()) {
				String key = getCallBackKey(builder.getInterfaceClass(), iServiceCallBack.getMethodName());
				iServiceCallBackMap.put(key, iServiceCallBack);
			}
		}

		//快速失败的回调方法
		if (CollectionUtil.isNotEmpty(builder.getFallbackList())) {
			for (Fallback fallback : builder.getFallbackList()) {
				String key = getCallBackKey(builder.getInterfaceClass(), fallback.getMethodName());
				fallbackMap.put(key, fallback);
			}
		}
	}

	/**
	 * 添加客户端默认的过滤器
	 * @param filters 过滤器
	 */
	private void addDefaultFilter(List<RpcFilter> filters) {
		filters.add(new RpcLogFilter());
		filters.add(new RpcParamValidateFilter());
	}

	/**
	 * 获取callback key
	 * @param interfaceClass rpc service 类
	 * @param methodName 执行方法名
	 * @return String
	 */
	private String getCallBackKey(Class interfaceClass, String methodName) {
		String key = interfaceClass.getName() + "@" + methodName;
		return key;
	}


	/**
	 * 构造请求对象
	 * @param method 远程方法对象
	 * @param args 参数
	 * @return RpcRequest rpc 请求对象
	 * @throws Exception
	 */
	private RpcRequest buildRequest(Method method, Object[] args) throws Exception {
		//准备请求数据
		RpcService service = (RpcService) builder.getInterfaceClass().getAnnotation(RpcService.class);
		if (service == null) {
			String error = "not rpc server interface,it must has annotation @RpcService";
			log.error(error);
			throw new UncheckedException(error);
		}
		// 设置请求报文参数
		RpcRequest request = new RpcRequest();
		//业务请求
		request.setMsgFlag(MsgFlag.BUSINESS.getValue());
		RpcHeader header = InvocationContext.getHeader();
		header.setMsgId(InvocationContext.getTraceId());
		//默认使用protostuff序列化
		//				header.setSerialType(SerialType.PROTOSTUFF.getValue());
		header.setSerialType(SerialType.JSON.getValue());
		//默认的rpc协议版本
		header.setVersion(RpcConstants.VERSION_1);
		header.setServerVersion(service.version());
		header.setTimeout(service.timeout());
		header.setClientIp(NetUtil.getLocalHost());
		if (builder.isBroadcast()) {
			//广播模式
			header.setRequestMode(RequestMode.BROADCAST.name());
		} else {
			header.setRequestMode(RequestMode.P2P.name());
		}

		//获取客户端服务的app.name(一般使用域名，如：www.payment.xxx.com)
		String clientNamespace = PropertyManager.get(RpcConstants.RPC_APP_NAME);
		header.setClientNamespace(clientNamespace);
		String namespace = namespaceMap.get(builder.getInterfaceClass());
		if (StringUtil.isEmpty(namespace)) {
			namespace = (String) ReflectUtil.getFinalFieldValue(builder.getInterfaceClass(),
					RpcConstants.RPC_SERVER_NAMESPACE_KEY);
			if (StringUtil.isNotEmpty(namespace)) {
				namespaceMap.put(builder.getInterfaceClass(), namespace);
			} else if (StringUtil.isNotEmpty(builder.getServerNamespace())) {
				namespaceMap.put(builder.getInterfaceClass(), builder.getServerNamespace());
			}
		}
		namespace = namespaceMap.get(builder.getInterfaceClass());
		header.setNamespace(namespace);//目标服务的命名空间
		String host = PropertyManager.get(RpcConstants.RPC_APP_SERVER_HOST_KEY);
		String port = PropertyManager.get(RpcConstants.RPC_APP_SERVER_PORT_KEY);
		header.setServerIp(host);
		header.setServerPort(port);
		Server server = InvocationContext.getServer();
		if (server != null) {
			header.setServerIp(server.getHost());
			header.setServerPort(String.valueOf(server.getPort()));
		}
		RpcMethod rpcMethod = method.getAnnotation(RpcMethod.class);
		if (rpcMethod != null && rpcMethod.timeout() != 0) {
			header.setTimeout(rpcMethod.timeout());
		}

		header.setMethodName(method.getName());
		header.setServiceName(builder.getInterfaceClass().getName());
		header.setSignAlgorithm(SignAlgorithm.SHA256withRSA.name());
		header.setRequestTime(System.currentTimeMillis());
		header.setMethodReturnType(method.getReturnType().getName());
		header.setKeepalive(true);
		request.setHeader(header);
		String idc = PropertyManager.get(RpcConstants.RPC_APP_IDC);
		header.setClientIdc(idc);
		String regionId = PropertyManager.get(RpcConstants.RPC_APP_REGION_ID);
		header.setClientRegionId(regionId);
		//当前客户端服务的角色
		String serverType = PropertyManager.get(RpcConstants.RPC_SERVER_ROLE_KEY,
				RpcServerType.BUSINESS_SERVER.name());
		header.setClientRole(serverType);

		//自定义编解码器
		if(builder.getRequestCoder() != null){
			header.setRequestCoderClassName(builder.getRequestCoder().getClass().getName());
			RpcHelper.addCoder(builder.getRequestCoder());
		}
		if(builder.getResponseCoder() != null){
			header.setResponseCoderClassName(builder.getResponseCoder().getClass().getName());
			RpcHelper.addCoder(builder.getRequestCoder());
		}

		//把参数都放到list，服务端会对list解包还原
		List list = Lists.newArrayList();
		if (method.getParameterTypes().length > 1) {
			String paramTypes = methodTypesMap.get(method);
			if (StringUtil.isEmpty(paramTypes)) {
				Class<?>[] clzTypes = method.getParameterTypes();
				paramTypes = RpcHelper.getParamTypesStr(clzTypes);
				//缓存方法类型
				methodTypesMap.put(method, paramTypes);
			}
			for (Object arg : args) {
				if (arg != null) {
					list.add(arg);
				} else {
					if (request.getHeader().getSerialType() != SerialType.PROTOSTUFF.getValue()) {
						list.add(null);
					} else {
						//PROTOSTUFF 序列化的时候，空值需要用NullValue对象替代，否则在解码的时候，List中的null就丢失
						list.add(new NullValue());
					}
				}
			}
			request.getHeader().setMethodParamTypes(paramTypes);
		} else if (method.getParameterTypes().length == 1) {
			//只有一个参数的情况
			if (args[0] != null) {
				list.add(args[0]);
			} else {
				if (request.getHeader().getSerialType() != SerialType.PROTOSTUFF.getValue()) {
					list.add(null);
				} else {
					list.add(new NullValue());
				}
			}
			header.setMethodParamTypes(method.getParameterTypes()[0].getName());
		} else {
			//方法没有参数的情况
			header.setMethodParamTypes(RpcConstants.NULL);
		}
		//把方法的参数都放到list集合中，服务端会对list解包
		request.setBody(list);
		header.setMethodId(RpcHelper.getMethodId(header.getServiceName(), header.getMethodName(),
				header.getMethodParamTypes()));
		RpcHelper.addMethod(header.getMethodId(), method);
		//指定本次rpc 请求是否为同步请求
		header.setSyncRequest(builder.isSyncRequest());

		if (!header.isSyncRequest()) {
			//异步请求
			String key = getCallBackKey(builder.getInterfaceClass(), method.getName());
			IServiceCallBack callBack = iServiceCallBackMap.get(key);
			if (callBack != null) {
				MessageHelper.addCallback(header.getMsgId(), response -> callBack.call(response.getBody()));
			}
		}

		return request;
	}

	/**
	 * 远程调用
	 * @param proxy 代理对象
	 * @param method 远程方法对象
	 * @param args 请求参数
	 * @return 请求结果
	 * @throws Throwable 异常
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		log.debug("aop proxy invoke ,method:{}", method.getName());
		if ("toString".equalsIgnoreCase(method.getName())) {
			//jdk 动态代理默认执行的方法
			return builder.getInterfaceClass().getName();
		}
		if ("equals".equalsIgnoreCase(method.getName())) {
			//spring 实例化动态代理类的时候，会默认执行该方法
			return true;
		}

		PerformanceUtil.start(this.getClass().getSimpleName());
		//构建请求对象
		RpcRequest request = buildRequest(method, args);

		//返回的数据结构
		FilterChain chain = new RpcFilterChain();
		((RpcFilterChain) chain).init(processor, filters);
		RpcResponse response = new RpcResponse();
		chain.doFilter(request, response);

		if (!request.getHeader().isSyncRequest()) {
			if (StringUtil.equalsIgnoreCase("void", request.getHeader().getMethodReturnType())) {
				return null;
			}
			return RpcHelper.newInstance(request.getHeader().getMethodReturnType());
		}

		log.debug("get response cost:{}", PerformanceUtil.end(this.getClass().getSimpleName()));
		//解析响应结果
		return parseResponse(response, method);
	}


	/**
	 * 解析响应对象
	 * @param response 响应对象
	 * @return Object
	 */
	private Object parseResponse(RpcResponse response, Method method) {
		// 处理响应结果
		if (response.getHeader() == null) {
			throw new UncheckedException("error response");
		}
		if (method.getReturnType().equals(java.lang.Void.class)) {
			return null;
		}
		if (!StringUtil.equalsIgnoreCase(RpcError.SUCCESS.getErrorCode(), response.getHeader().getRespCode())) {
			if (response.getHeader().isFallback()) {
				//异步请求,快速失败处理响应
				String key = getCallBackKey(builder.getInterfaceClass(), method.getName());
				Fallback fallback = fallbackMap.get(key);
				if (fallback != null) {
					Object value = fallback.doResponse();
					ClientThreadPoolFactory.getClientExecutor().execute(() -> {
						fallback.callback(value);
					});
					return value;
				}
			}
			//接口服务发生熔断
			log.warn("service:{} method:{} has circuited", response.getHeader().getServiceName(),
					response.getHeader().getMethodName());
			throw new RpcException(response.getHeader().getRespCode(), response.getHeader().getRespMsg(),
					response.getHeader());
		}
		if (response.getHeader().isMockResponse()) {
			//如果该请求被挡板拦截（也就是做了模拟结果）
			if (method.getReturnType().isArray()) {
				List resp = JSON.parseArray((String) response.getBody(), method.getReturnType());
				return resp.toArray();
			} else if (RpcHelper.isList(method.getReturnType())) {
				Type[] returnTypes = ReflectUtil.getGenericReturnType(method);
				if (returnTypes != null && returnTypes.length == 1) {
					List resp = JSON.parseArray((String) response.getBody(), returnTypes[0].getClass());
					return resp;
				} else {
					List resp = JSON.parseArray((String) response.getBody(), method.getReturnType());
					return resp;
				}

			} else {
				return JSON.parseObject((String) response.getBody(), method.getReturnType());
			}

		}

		//list集合特殊处理
		if (RpcHelper.isList(method.getReturnType())) {
			RpcBody body = (RpcBody) response.getBody();
			if (body == null) {
				return null;
			}
			if (response.getHeader().getSerialType() == SerialType.JSON.getValue()) {
				if (response.getBody() instanceof JSONArray) {
					Type[] returnTypes = ReflectUtil.getGenericReturnType(method);
					if (returnTypes != null && returnTypes.length == 1) {
						return ((JSONArray) response.getBody()).toJavaList(returnTypes[0].getClass());
					}
					return ((JSONArray) response.getBody()).toJavaList(method.getReturnType());
				}
			}
			return body.getBody();
		}
		if (response.getHeader().getSerialType() == SerialType.JSON.getValue()) {
			if (response.getBody() != null) {
				//如果序列化方式是json，还需要特殊处理
				if (response.getBody() instanceof JSONObject) {
					return ((JSONObject) response.getBody()).toJavaObject(method.getReturnType());
				}
			} else {
				log.warn("error request,no result response to client,service:{} ,methodName:{}",
						response.getHeader().getServiceName(), response.getHeader().getMethodName());
				if (RpcHelper.isSimpleType(method.getReturnType())) {
					throw new RpcException(RpcError.INVAILD_REQUEST, "no result response to client");
				}
			}
		}
		return response.getBody();
	}

	public static Builder create() {

		return new Builder();
	}
}
