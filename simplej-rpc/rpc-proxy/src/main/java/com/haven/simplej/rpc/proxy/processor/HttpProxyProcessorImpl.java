package com.haven.simplej.rpc.proxy.processor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.http.HttpBuilder;
import com.haven.simplej.http.HttpExecuter;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.constant.HttpField;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.*;
import com.haven.simplej.rpc.protocol.processor.IServiceProcessor;
import com.haven.simplej.rpc.protocol.response.ResponseHelper;
import com.haven.simplej.rpc.proxy.enums.HttpMethod;
import com.haven.simplej.rpc.proxy.helper.ProxyHelper;
import com.haven.simplej.rpc.proxy.http.model.HttpRequest;
import com.haven.simplej.rpc.proxy.http.model.HttpResponse;
import com.haven.simplej.rpc.proxy.manager.ServiceManager;
import com.haven.simplej.rpc.route.InstanceSelect;
import com.haven.simplej.rpc.server.processor.BaseProcessor;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.logging.PerformanceUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * http协议转发处理器
 * @TODO 待优化
 * @author: havenzhang
 * @date: 2018/5/16 23:48
 * @version 1.0
 */
@Data
@Slf4j
public class HttpProxyProcessorImpl extends BaseProcessor implements IServiceProcessor {

	/**
	 * 默认的连接超时时间
	 */
	private static final int DEFAULT_CONNECT_TIMEOUT = 3000;
	/**
	 * 默认socket超时时间
	 */
	private static final int DEFAULT_SOCKET_TIMEOUT = 30000;
	/**
	 * 默认编码
	 */
	private static final String DEFAULT_ENCODE = "utf-8";

	/**
	 * 接收http请求，并执行远程方法的uri
	 */
	private static final String HTTP_2_RPC_URI = "/rpc/execute";
	/**
	 * http
	 */
	private static final String HTTP_SCHEMA = "http";

	/**
	 * 默认content-type
	 */
	private static final String DEFAULT_CONTENT_TYPE = "text/html";
	/**
	 * http客户端缓存
	 */
	private static final Map<HttpBuilder, HttpExecuter> clientMap = Maps.newConcurrentMap();

	@Autowired
	private InstanceSelect instanceSelect;

	@Autowired
	private ServiceManager serviceManager;


	@Override
	public RpcResponse process(RpcRequest request) {
		PerformanceUtil.start(this.getClass().getSimpleName());
		HttpRequest httpReq = (HttpRequest) request;
		RpcResponse response = new RpcResponse();
		HttpResponse httpResponse = new HttpResponse();
		CloseableHttpResponse resp = null;
		try {
			Map<String, String> header = httpReq.getHttpHeader();
			HttpExecuter executer = getHttpExecuter(httpReq);
			List<NameValuePair> parameters = Lists.newArrayList();
			httpReq.getReqParams().forEach((k, v) -> {
				NameValuePair pair = new BasicNameValuePair(k, v);
				parameters.add(pair);
			});

			RpcHeader rpcHeader = ProxyHelper.getRpcHeader(httpReq);
			if (ProxyHelper.toRpcServer(header)) {
				//tcp服务
				//http(app)-->http(proxy) -->http(proxy) --> [http -->rpc service实例]
				//复制参数
				copyParam(parameters, rpcHeader);
				String methodId = rpcHeader.getMethodId();
				String serviceVersion = rpcHeader.getServerVersion();
				//获取service 查找key
				ServiceLookupKey lookupKey = getLookupKey(rpcHeader);
				//2.本地proxy没有该服务的话，再查下远程注册列表，判断是否存在该请求服务
				ServiceInstance instance = null;
				ServiceInfo serviceInfo;
				if (StringUtil.isEmpty(methodId)) {
					List<ServiceInstance> instances = RpcHelper.getServiceInstance(methodId, serviceVersion);
					if (CollectionUtil.isNotEmpty(instances)) {
						//负载均衡
						instance = instanceSelect.getInstance(instances, rpcHeader);
					}
				}
				//如果methodId找不到服务信息，则继续查询
				if (instance == null) {
					serviceInfo = serviceManager.getService(lookupKey);
					if (serviceInfo == null) {
						return ResponseHelper.buildResponse(RpcError.SERVICE_NOT_FOUND,
								"error service request,can " + "not" + " find " + "service by " + lookupKey,
								request.getHeader());
					}
					//负载均衡
					instance = instanceSelect.getInstance(serviceInfo.getInstances(), rpcHeader);
				}
				//如果还是找不到，那么就抛异常了
				if (instance == null) {
					return ResponseHelper.buildResponse(RpcError.SERVICE_INSTANCE_NOT_FOUND, "instance can not be " +
							"found " + "service by " + lookupKey, request.getHeader());
				}
				int port = instance.getProxyHttpPort();
				boolean proxyToPorxy = PropertyManager.getBoolean(RpcConstants.RPC_PROXY_TO_PROXY_KEY, false);
				if (instance.isLocal() || !proxyToPorxy) {
					port = instance.getHttpPort();
				}
				StringBuilder url = new StringBuilder();
				url.append(HTTP_SCHEMA).append("://");
				url.append(instance.getHost()).append(":").append(port);
				url.append(HTTP_2_RPC_URI);//每个rpc server服务上都有该url服务，用于将http请求转成本地方法调用
				log.debug("getTargetUrl:{}", url.toString());
				resp = executer.postForm2(url.toString(), parameters, header);
			} else {
				// http --> http服务Controller
				String method = httpReq.getHttpMethod();
				//获取目标url
				String targetUrl;
				if (StringUtil.startsWith(httpReq.getHttpUrl(), HTTP_SCHEMA)) {
					//如果是以http开头的url，则直接请求
					targetUrl = httpReq.getHttpUrl();
				} else {
					//负载均衡
					targetUrl = serviceManager.getTargetUrl(httpReq);
				}
				//http请求
				if (StringUtil.equalsIgnoreCase(HttpMethod.GET.name(), method)) {
					resp = executer.get2(targetUrl, convert2queryString(httpReq.getReqParams()),
							httpReq.getHttpHeader());
				} else if (StringUtil.equalsIgnoreCase(HttpMethod.POST.name(), method) && httpReq.getStream() != null) {
					//上传流
					resp = executer.post2(targetUrl, httpReq.getStream(), httpReq.getHttpHeader());
				} else if (StringUtil.equalsIgnoreCase(HttpMethod.POST.name(), method) && httpReq.getStream() == null) {
					resp = executer.postForm2(targetUrl, parameters, header);
				}
			}

			//转换响应结果
			convert(httpResponse, resp);
		} catch (IOException e) {
			log.error("dispatch error", e);
		} finally {
			try {
				if (null != resp) {
					EntityUtils.consume(resp.getEntity());
					resp.close();
				}
			} catch (IOException e) {
				log.error("close response error", e);
			}
		}
		response.setBody(httpResponse);
		log.debug("http proxy dispatch request cost:{}", PerformanceUtil.end(this.getClass().getSimpleName()));
		return response;
	}

	/**
	 * 获取http 客户端实例
	 * @param httpReq httpReq
	 * @return HttpExecuter
	 */
	private HttpExecuter getHttpExecuter(HttpRequest httpReq) {
		Map<String, String> header = httpReq.getHttpHeader();
		String encoding = header.getOrDefault(HttpField.encoding, DEFAULT_ENCODE);
		String contentType = header.getOrDefault(HttpField.contentType, DEFAULT_CONTENT_TYPE);
		boolean keepalive = Boolean.parseBoolean(header.getOrDefault(HttpField.keepalive, "false"));
		int connectTimeout = Integer.parseInt(header.getOrDefault(HttpField.connectTimeout,
				String.valueOf(DEFAULT_CONNECT_TIMEOUT)));
		int socketTimeout = Integer.parseInt(header.getOrDefault(HttpField.socketTimeout,
				String.valueOf(DEFAULT_SOCKET_TIMEOUT)));

		HttpBuilder builder =
				HttpExecuter.create().setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).setEncoding(encoding).setKeepAlive(keepalive).setMimetype(contentType);
		HttpExecuter executer;
		if (clientMap.containsKey(builder)) {
			executer = clientMap.get(builder);
		} else {
			executer = builder.build();
		}
		return executer;
	}

	/**
	 * 复制参数
	 */
	private void copyParam(List<NameValuePair> parameters, RpcHeader rpcHeader) {

		NameValuePair pair = new BasicNameValuePair(HttpField.serviceName, rpcHeader.getServiceName());
		parameters.add(pair);

		pair = new BasicNameValuePair(HttpField.methodName, rpcHeader.getMethodName());
		parameters.add(pair);

		pair = new BasicNameValuePair(HttpField.methodParamTypes, rpcHeader.getMethodParamTypes());
		parameters.add(pair);

		pair = new BasicNameValuePair(HttpField.methodId, rpcHeader.getMethodId());
		parameters.add(pair);

		pair = new BasicNameValuePair(HttpField.serviceVersion, rpcHeader.getServerVersion());
		parameters.add(pair);
	}

	public String convert2queryString(Map<String, String> params) {

		StringBuilder queryString = new StringBuilder();
		params.forEach((k, v) -> queryString.append(k).append("=").append(v).append("&"));

		return queryString.substring(0, queryString.length() - 1);
	}

	/**
	 * http响应model转换
	 */
	private void convert(HttpResponse httpResponse, CloseableHttpResponse resp) throws IOException {
		byte[] content = EntityUtils.toByteArray(resp.getEntity());
		Header[] httpHeader = resp.getAllHeaders();
		httpResponse.setBody(content);
		Map<String, String> respHeader = Maps.newHashMap();
		for (Header header1 : httpHeader) {
			HeaderElement[] elements = header1.getElements();
			for (HeaderElement element : elements) {
				respHeader.put(element.getName(), element.getValue());
			}
		}
		httpResponse.setHeader(respHeader);
	}
}
