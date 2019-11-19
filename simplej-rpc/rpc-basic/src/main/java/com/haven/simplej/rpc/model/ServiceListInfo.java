package com.haven.simplej.rpc.model;

import lombok.Data;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2018/9/27 19:55
 * @version 1.0
 */
@Data
public class ServiceListInfo {

	/**
	 * 摘要信息，用于与本地判断信息是否发生变更
	 */
	private String md5;

	/**
	 * 服务列表
	 */
	List<ServiceInfo> serviceList;
}
