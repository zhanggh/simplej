package com.haven.simplej.rpc.enums;

/**
 * 业务自定义的filter，order请设置在300以上，[0-300]为框架内用
 * 过滤器的顺序,数值越小，优先级越高
 * @author: havenzhang
 * @date: 2019/1/28 16:46
 * @version 1.0
 */
public enum FilterOrder {

	/**
	 * 日志过滤器，参数校验过滤器，流量测量过滤器，权限控制
	 */
	METRIC_FILTER(1),
	AUTH_CHECK(2),
	CONFIG_LISTEN(3),
	LOG_FILTER(10),
	MOCK_FILTER(11),
	PARAM_VALIDATE_FILTER(100),
	NAMESPACE_FILTER(200);

	int order;

	FilterOrder(int order) {
		this.order = order;
	}

	public int order() {
		return this.order;
	}}
