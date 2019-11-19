package com.haven.simplej.rpc.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * 服务信息
 * @author: havenzhang
 * @date: 2019/1/10 17:28
 * @version 1.0
 */
@Data
public class ServiceMeta {

	/**
	 * 服务版本号
	 */
	private String version;

	/**
	 * 服务超时时间
	 */
	private long timeout;

	/**
	 * 服务名
	 */
	private String serviceName;

	/**
	 * service文档描述
	 */
	private String serviceDoc;
	/**
	 * model代码创建的时间
	 */
	private String createTime;

	/**
	 * 作者
	 */
	private String author;

	/**
	 * 服务唯一id
	 */
	private String serviceId;

	/**
	 * 服务方法信息
	 */
	private List<MethodMeta> methods = Lists.newArrayList();
}
