package com.haven.simplej.rpc.mock.service;

import com.haven.simplej.rpc.annotation.Doc;

/**
 *
 * 服务治理中心域名或者命名空间
 * @author: havenzhang
 * @date: 2018/9/28 17:29
 * @version 1.0
 */
@Doc("每一个暴露出去的api都应该继承一个BaseRpcService，NAMESPACE的值设置为当前服务的域名（命名空间）")
public interface BaseRpcService {
	/**
	 * 服务治理中心（主控中心）的域名（命名空间），用于唯一标识该服务
	 */
	String NAMESPACE = "www.mock.rpc.simplej.com";

	default String getNamespace() {
		return NAMESPACE;
	}
}
