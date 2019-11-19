package com.haven.simplej.rpc.model;

import lombok.Data;

/**
 * 服务状态信息
 * @author: havenzhang
 * @date: 2018/10/10 0:07
 * @version 1.0
 */
@Data
public class ServiceStatus {

	/**
	 * 域名（命名空间）
	 */
	private String namespace;
	/**
	 * 服务名
	 */
	private String serviceName;

	/**
	 * 服务版本
	 */
	private String version;

	/**
	 * 服务状态，0-正常，1-短暂性故障，-1 -故障，无法正常服务
	 */
	private int status;

	/**
	 * 错误描述
	 */
	private String errorMsg;
}
