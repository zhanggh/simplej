package com.haven.simplej.rpc.model;

import lombok.Data;

import java.util.List;

/**
 * 健康检查响应model
 * @author: havenzhang
 * @date: 2018/5/6 22:23
 * @version 1.0
 */
@Data
public class HeathResponse<T> {
	/**
	 * 服务状态返回码：0000-正常，其他返回码均代码服务不正常
	 */
	private String respCode;
	/**
	 * 服务启动时间
	 */
	private String startUpTime;

	/**
	 * 服务所在的实例信息
	 */
	private ServiceInstance instance;

	/**
	 * 服务状态信息
	 */
	private List<ServiceStatus> statusList;

	/**
	 * 更多信息，如当前CPU，线程数，内存
	 */
	private T more;
}
