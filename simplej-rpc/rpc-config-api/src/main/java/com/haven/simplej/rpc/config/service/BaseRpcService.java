package com.haven.simplej.rpc.config.service;

import com.haven.simplej.rpc.annotation.Doc;

/**
 *
 * 配置中心
 * @author: havenzhang
 * @date: 2018/9/28 17:29
 * @version 1.0
 */
@Doc("每一个暴露出去的api都应该继承一个BaseRpcService，NAMESPACE的值设置为当前服务的域名（命名空间）")
public interface BaseRpcService {
	/**
	 * 配置中心（主控中心）的域名（命名空间），用于唯一标识该服务
	 */
	String NAMESPACE = "www.config.rpc.simplej.com";
}
