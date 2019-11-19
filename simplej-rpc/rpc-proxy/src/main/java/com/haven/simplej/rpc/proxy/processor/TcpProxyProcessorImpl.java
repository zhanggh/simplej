package com.haven.simplej.rpc.proxy.processor;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.enums.RequestMode;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.*;
import com.haven.simplej.rpc.protocol.processor.IServiceProcessor;
import com.haven.simplej.rpc.protocol.response.ResponseHelper;
import com.haven.simplej.rpc.proxy.helper.ProxyHelper;
import com.haven.simplej.rpc.proxy.limiter.RpcLimiter;
import com.haven.simplej.rpc.proxy.manager.RouteManager;
import com.haven.simplej.rpc.proxy.manager.ServiceManager;
import com.haven.simplej.rpc.route.InstanceSelect;
import com.haven.simplej.rpc.server.helper.ServiceInfoHelper;
import com.haven.simplej.rpc.server.netty.threadpool.ThreadPoolFactory;
import com.haven.simplej.rpc.server.processor.BaseProcessor;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.base.type.Pair;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.logging.PerformanceUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.List;

/**
 * tcp 请求proxy转发处理器
 *
 * @author: havenzhang
 * @date: 2018/5/26 17:47
 * @version 1.0
 */
@Slf4j
@Data
public class TcpProxyProcessorImpl extends BaseProcessor implements IServiceProcessor {

	@Autowired
	private ServiceManager serviceManager;

	@Autowired
	private InstanceSelect instanceSelect;

	@Autowired
	private RpcLimiter limiter;


	@Override
	public RpcResponse process(RpcRequest request) {
		PerformanceUtil.start(this.getClass().getSimpleName());
		//查找远程服务的key
		ServiceLookupKey lookupKey = getLookupKey(request.getHeader());
		ServiceInfo serviceInfo;
		RpcHeader header = request.getHeader();
		String methodId = header.getMethodId();
		String serviceVersion = header.getServerVersion();

		//本地查找服务的时候，没有namespace也不影响查找
		if (StringUtil.isEmpty(methodId)) {
			//本地实例查询（不需要加版本号，因为本地只会有一个版本）
			Pair<Class, Method> pair = ServiceInfoHelper.getServiceAndMethod(methodId);
			if (pair != null) {
				//比如proxy自身的健康检查服务，当然还可以有更多
				return invokeLocalMethod(null, request);
			}
		} else {
			//1.从本地缓存中查询服务信息
			serviceInfo = getServiceInfo(lookupKey);
			if (serviceInfo != null) {
				//比如proxy自身的健康检查服务，当然还可以有更多
				return invokeLocalMethod(serviceInfo, request);
			}
		}
		//2.本地proxy没有该服务的话，再查下远程注册列表，判断是否存在该请求服务
		List<ServiceInstance> instances;
		RpcResponse response;
		if (StringUtil.isNotEmpty(methodId)) {
			if (ProxyHelper.isRegisterMethod(methodId)) {
				instances = RouteManager.getRegisterInstances();
			} else {
				//优先使用methodId查找服务信息
				instances = RpcHelper.getServiceInstance(methodId, serviceVersion);
			}
		} else {
			if (ProxyHelper.isRegisterMethod(lookupKey.getServiceName())) {
				instances = RouteManager.getRegisterInstances();
			} else {
				serviceInfo = serviceManager.getService(lookupKey);
				if (serviceInfo == null) {
					return ResponseHelper.buildResponse(RpcError.SERVICE_NOT_FOUND,
							" instance of " + header.getServiceName() + " can not be found ", request.getHeader());
				}
				instances = serviceInfo.getInstances();

			}
		}
		if (CollectionUtil.isEmpty(instances)) {
			log.warn("methodId:{},methodName:{},serviceName:{} can not be found,pls check", header.getMethodId(),
					header.getMethodName(), header.getServiceName());
			return ResponseHelper.buildResponse(RpcError.SERVICE_INSTANCE_NOT_FOUND,
					" instance of " + header.getServiceName() + " can not be found ", request.getHeader());
		}

		if (StringUtil.equalsIgnoreCase(header.getRequestMode(), RequestMode.BROADCAST.name())) {
			//广播模型
			for (ServiceInstance serviceInstance : instances) {
				//异步广播
				ThreadPoolFactory.getServerExecutor().execute(() -> send(serviceInstance, request));
			}
			request.getHeader().getAddition().put("broadcast_instances", JSON.toJSONString(instances));
			response = ResponseHelper.buildResponse(RpcError.SUCCESS, "BROADCAST success", request.getHeader());
		} else {
			//点对点请求
			response = send(instances, request);
		}
		return response;
	}

	/**
	 * 带流量控制的请求
	 * @param instances 目标可选实例
	 * @param request 请求
	 * @return RpcResponse
	 */
	private RpcResponse send(List<ServiceInstance> instances, RpcRequest request) {

		RpcResponse response;
		//点对点请求 负载均衡
		ServiceInstance instance = instanceSelect.getInstance(instances, request.getHeader());
		//流控检查
		String limitKey = getLimitKey(instance);
		boolean pass = limiter.tryAquire(limitKey, 1, request.getHeader().getTimeout());
		if (pass) {
			try {
				response = send(instance, request);
			} finally {
				limiter.release(limitKey, 1);
			}
		} else {
			//后面改成set
			instances.remove(instance);
			if (CollectionUtil.isEmpty(instances)) {
				//超过流控限制
				log.debug("over flow limit :{}", limiter.getFlowLimit(limitKey));
				response = ResponseHelper.buildResponse(RpcError.SUCCESS,
						"over flow limit:" + limiter.getFlowLimit(limitKey), request.getHeader());
				return response;
			}
			return send(instances, request);
		}
		log.debug("proxy dispatch request cost:{}", PerformanceUtil.end(this.getClass().getSimpleName()));
		return response;
	}

	public static String getLimitKey(ServiceInstance instance) {

		return instance.getMd5();
	}
}
