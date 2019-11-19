package com.haven.simplej.rpc.mock.model;

import com.google.common.collect.Lists;
import com.haven.simplej.rpc.annotation.RpcStruct;
import com.haven.simplej.rpc.model.MethodInfo;
import com.haven.simplej.rpc.model.ServiceInstance;
import lombok.Data;

import java.util.List;

/**
 * 方法挡板响应信息
 * @author: havenzhang
 * @date: 2018/9/27 17:41
 * @version 1.0
 */
@Data
@RpcStruct
public class MockMethod extends MethodInfo {


	/**
	 * 模拟的响应
	 * 响应报文，必须是json报文
	 */
	private String response;

	/**
	 * mock 的实例
	 */
	private List<ServiceInstance> instances = Lists.newArrayList();
}
