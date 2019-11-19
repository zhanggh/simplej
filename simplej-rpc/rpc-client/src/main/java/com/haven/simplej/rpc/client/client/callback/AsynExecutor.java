package com.haven.simplej.rpc.client.client.callback;

import com.haven.simplej.rpc.client.client.helper.ClientHelper;
import com.haven.simplej.rpc.client.client.proxy.Builder;
import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;

import java.util.List;

/**
 * 异步rpc执行器
 * @author: havenzhang
 * @date: 2019/9/9 19:54
 * @version 1.0
 */
public class AsynExecutor {

	/**
	 * 获取rpc客户端实例，通过该工具获取的客户端实例，执行rpc调用都是异步的
	 * @param serviceClz rpc api 类
	 * @return 代理实例, 单例
	 */
	public static <T> T buildService(Class<T> serviceClz, List<IServiceCallBack> callList) {
		T service;
		Builder builder = ServiceProxy.create();
		builder.setSyncRequest(false).setInterfaceClass(serviceClz).addCallBack(callList);
		builder.setSyncRequest(false);
		// 设置当前线程的请求为异步请求
		service = builder.setInterfaceClass(serviceClz).build();

		return service;
	}

}
