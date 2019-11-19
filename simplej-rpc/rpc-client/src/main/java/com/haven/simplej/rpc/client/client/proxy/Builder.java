package com.haven.simplej.rpc.client.client.proxy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haven.simplej.rpc.callback.Fallback;
import com.haven.simplej.rpc.client.client.callback.IServiceCallBack;
import com.haven.simplej.rpc.client.client.helper.ClientHelper;
import com.haven.simplej.rpc.client.client.processor.ClientProcessorImpl;
import com.haven.simplej.rpc.client.client.threadpool.ClientThreadPoolFactory;
import com.haven.simplej.rpc.exception.RpcError;
import com.haven.simplej.rpc.exception.RpcException;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.protocol.coder.Coder;
import com.haven.simplej.rpc.protocol.processor.IServiceProcessor;
import com.haven.simplej.rpc.transaction.TransactionManager;
import com.haven.simplej.spring.SpringContext;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.Data;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * 构造器模式
 * @author: havenzhang
 * @date: 2018/01/28 22:20
 * @version 1.0
 */
@Data
public class Builder {


	/**
	 * 代理缓存
	 */
	private static Map<Builder, ServiceProxy> serviceProxyMap = Maps.newConcurrentMap();

	/**
	 * 过滤器
	 */
	private List<RpcFilter> filters;

	/**
	 * 快速失败的钩子
	 */
	private List<Fallback> fallbackList = Lists.newArrayList();

	/**
	 * 业务处理器
	 */
	private IServiceProcessor processor;

	/**
	 * 接口class
	 */
	private Class interfaceClass;
	/**
	 * 是否同步请求
	 */
	private boolean syncRequest = true;

	/**
	 * 是否使用单例
	 */
	private boolean singleInstance = true;

	/**
	 * 是否广播请求
	 */
	private boolean broadcast;

	/**
	 * 服务端命名空间
	 */
	private String serverNamespace;

	/**
	 * 自定义的请求参数的编解码器（假如需要自定义协议的话）
	 */
	private Coder requestCoder;

	/**
	 * 响应结果编解码器
	 */
	private Coder responseCoder;

	/**
	 * 业务回调
	 */
	private List<IServiceCallBack> callBackList = Lists.newArrayList();

	/**
	 * 分布式事务管理器
	 */
	private TransactionManager transactoinManager;

	protected Builder() {
		super();
	}

	/**
	 * 远程接口类
	 * @param interfaceClass 接口类
	 * @return Builder
	 */
	public Builder setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
		return this;
	}

	/**
	 * 指定请求的目标服务的命名空间
	 * @param serverNamespace 目标服务的命名空间
	 * @return Builder
	 */
	public Builder setServerNamespace(String serverNamespace) {
		this.serverNamespace = serverNamespace;
		return this;
	}

	public Builder setTransactoinManager(TransactionManager transactoinManager) {
		this.transactoinManager = transactoinManager;
		return this;
	}

	/**
	 * 客户端请求是否为同步请求
	 * @param sync true-同步请求，false-异步请求
	 * @return Builder
	 */
	public Builder setSyncRequest(boolean sync) {
		this.syncRequest = sync;
		return this;
	}

	/**
	 * 是否启动广播
	 * @param broadcast 是否启动广播
	 * @return Builder
	 */
	public Builder setBroadcast(boolean broadcast) {
		this.broadcast = broadcast;
		return this;
	}

	public Builder isSingleIntance(boolean singleInstance) {
		this.singleInstance = singleInstance;
		return this;
	}
	/**
	 * 增加异步回调的服务
	 * @param callBack 异步回调接口实现
	 * @return Builder
	 */
	public Builder addCallBack(List<IServiceCallBack> callBack) {
		this.callBackList.addAll(callBack);
		return this;
	}

	/**
	 * 增加异步回调的服务
	 * @param callBack 异步回调接口实现
	 * @return Builder
	 */
	public Builder addCallBack(IServiceCallBack callBack) {
		this.callBackList.add(callBack);
		return this;
	}


	/**
	 * 增加远程服务接口故障时的回退钩子
	 * @param fallback 回退处理的钩子接口
	 * @return Builder
	 */
	public Builder addFallback(Fallback fallback) {
		this.fallbackList.add(fallback);
		return this;
	}

	/**
	 * 自定义请求参数（对象）的编解码器
	 * @param requestCoder 编解码器
	 * @return Builder
	 */
	public Builder setRequestCoder(Coder requestCoder) {
		this.requestCoder = requestCoder;
		return this;
	}

	/**
	 * 指定对响应结果的编解码器
	 * @param responseCoder  编解码器
	 * @return Builder
	 */
	public Builder setResponseCoder(Coder responseCoder) {
		this.responseCoder = responseCoder;
		return this;
	}


	/**
	 * 构造接口代理实例
	 * @return 代理对象
	 */
	public <T> T build() {
		return getService(this);
	}

	/**
	 * 获取接口代理实例
	 * @return 代理实例
	 */
	public static synchronized <T> T getService(Builder builder) {
		if (builder.interfaceClass == null) {
			throw new RpcException(RpcError.SYSTEM_BUSY, "interface class of proxy can not be empty");
		}
		if (serviceProxyMap.containsKey(builder)) {
			return (T) serviceProxyMap.get(builder).getInterfaceService();
		}
		//初始化客户端连接
		ClientThreadPoolFactory.getClientExecutor().execute(()->ClientHelper.initClient());

		ClassLoader classLoader = builder.interfaceClass.getClassLoader();
		Class<?>[] interfaces = new Class[]{builder.interfaceClass};
		if (builder.processor == null) {
			builder.processor = new ClientProcessorImpl();
		}
		List<RpcFilter> filters = SpringContext.getBeansOfType(RpcFilter.class);
		if (CollectionUtil.isEmpty(builder.filters)) {
			builder.setFilters(filters);
		} else {
			builder.getFilters().addAll(filters);
		}
		ServiceProxy proxy = new ServiceProxy(builder);
		T obj = (T) Proxy.newProxyInstance(classLoader, interfaces, proxy);
		proxy.setInterfaceService(obj);
		serviceProxyMap.put(builder, proxy);
		return obj;
	}
}