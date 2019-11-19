package com.haven.simplej.rpc.server.http;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.annotation.RpcMethod;
import com.haven.simplej.rpc.registry.service.IServiceRegister;
import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.server.helper.ServiceInfoHelper;
import com.haven.simplej.rpc.model.UrlInfo;
import com.haven.simplej.rpc.model.UrlLookupKey;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.protocol.context.InvocationContext;
import com.haven.simplej.spring.SpringContext;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.base.type.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;

/**
 * web服务url管理器
 * @author: havenzhang
 * @date: 2019/5/19 22:52
 * @version 1.0
 */
@Slf4j
public class WebUrlManager {


	/**
	 * http url映射handler，可以获取到当前web服务的所有url
	 */
	private RequestMappingHandlerMapping handlerMapping;

	/**
	 * 当前web服务的所有url信息
	 */
	private static final List<UrlInfo> urlInfoList = Lists.newArrayList();

	/**
	 * lookupKey  和url信息的映射关系缓存
	 */
	private static final Map<UrlLookupKey, UrlInfo> urlMap = Maps.newHashMap();

	/**
	 * 注册服务接口
	 */
	private IServiceRegister serviceRegister;

	private static WebUrlManager manager;

	private WebUrlManager() {
		handlerMapping = SpringContext.getBean(RequestMappingHandlerMapping.class);
		serviceRegister = ServiceProxy.create().setInterfaceClass(IServiceRegister.class).build();
	}

	public static WebUrlManager getInstance() {
		if (manager == null) {
			synchronized (WebUrlManager.class) {
				if (manager == null) {
					manager = new WebUrlManager();
				}
			}
		}
		return manager;
	}

	public void initWebUrlInfo() {
		String namespace = PropertyManager.get(RpcConstants.RPC_APP_NAME);
		if (StringUtil.isEmpty(namespace)) {
			throw new UncheckedException("namespace can not be empty");
		}
		Map<RequestMappingInfo, HandlerMethod> methodMap = handlerMapping.getHandlerMethods();
		methodMap.forEach((k, v) -> {
			RequestMapping requestMapping = v.getMethodAnnotation(RequestMapping.class);
			String[] uris = requestMapping.value();
			for (String uri : uris) {
				if (requestMapping.method().length > 0) {//POST 、GET、PUT 、DELETE
					for (RequestMethod requestMethod : requestMapping.method()) {
						if (requestMapping.headers().length > 0) {
							for (String header : requestMapping.headers()) {
								Pair<UrlLookupKey, UrlInfo> pair = build(uri, namespace, requestMethod.name(), header,v);
								urlMap.put(pair.getLeft(), pair.getRight());
								urlInfoList.add(pair.getRight());
							}
						} else {
							Pair<UrlLookupKey, UrlInfo> pair = build(uri, namespace, requestMethod.name(), UrlLookupKey.ANY, v);
							urlMap.put(pair.getLeft(), pair.getRight());
							urlInfoList.add(pair.getRight());
						}

					}
				} else {//默认情况，即支持所有http 方法
					if (requestMapping.headers().length > 0) {
						for (String header : requestMapping.headers()) {
							Pair<UrlLookupKey, UrlInfo> pair = build(uri, namespace, UrlLookupKey.ANY, header, v);
							urlMap.put(pair.getLeft(), pair.getRight());
							urlInfoList.add(pair.getRight());
						}
					} else {
						Pair<UrlLookupKey, UrlInfo> pair = build(uri, namespace, UrlLookupKey.ANY, UrlLookupKey.ANY, v);
						urlMap.put(pair.getLeft(), pair.getRight());
						urlInfoList.add(pair.getRight());
					}
				}
			}
		});
	}



	private Pair<UrlLookupKey, UrlInfo> build(String uri, String namespace, String method, String header,
			HandlerMethod handlerMethod) {
		RpcMethod rpcMethod = handlerMethod.getMethod().getAnnotation(RpcMethod.class);
		UrlLookupKey lookupKey = new UrlLookupKey();
		UrlInfo urlInfo = new UrlInfo();
		lookupKey.setUri(uri);
		lookupKey.setNamespace(namespace);
		lookupKey.setHttpMethod(method);
		lookupKey.setHeader(header);
		if(rpcMethod != null){
			lookupKey.setVersion(rpcMethod.version());
		}
		BeanUtils.copyProperties(lookupKey, urlInfo);
		ServiceInstance instance = ServiceInfoHelper.getLocalInstance(namespace);
		List<ServiceInstance> instanceList = Lists.newArrayList();
		instanceList.add(instance);
		String schema = PropertyManager.get(RpcConstants.WEB_SERVER_SCHEMA_KEY, RpcConstants.WEB_SERVER_DEFAULT_SCHEMA);
		urlInfo.setSchema(schema);
		if(rpcMethod != null){
			urlInfo.setTimeout(rpcMethod.timeout());
		}
		urlInfo.getInstances().addAll(instanceList);
		Pair<UrlLookupKey, UrlInfo> pair = new Pair<>(lookupKey, urlInfo);
		return pair;
	}


	/**
	 * 发布服务元信息以及实例信息
	 * 该请求发送至proxy，由proxy进行上报
	 * @return boolean
	 */
	public boolean register() {
		try {
			initWebUrlInfo();
			if (StringUtil.isEmpty(PropertyManager.get(RpcConstants.RPC_PROXY_HOST_KEY))) {
				log.info("rpc.proxy.host not set ,do not register local services");
				return false;
			}

			log.debug("local serviceList:{}", JSON.toJSONString(urlInfoList, true));
			boolean result = serviceRegister.registerUri(urlInfoList);
			return result;
		} catch (Exception e) {
			log.error("register service error", e);
		} finally {
			InvocationContext.removeServerInfo();
		}
		return false;
	}

	public List<UrlInfo> getUrlInfoList(){
		return urlInfoList;
	}

	public UrlInfo getUrlInfo(UrlLookupKey lookupKey){

		return urlMap.get(lookupKey);
	}

}
