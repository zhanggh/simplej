package com.haven.simplej.rpc.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.haven.simplej.security.DigestUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 服务信息
 * @author haven.zhang
 * @date 2019/2/3.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ServiceInfo extends ServiceLookupKey {


	/**
	 * 方法
	 */
	private Set<MethodInfo> methods = Sets.newHashSet();

	/**
	 * 实例
	 */
	private List<ServiceInstance> instances = Lists.newArrayList();

	/**
	 * 服务类型：BUSINESS_SERVER-普通业务服务，PROXY-代理服务，RPC_CENTER-rpc主控服务
	 */
	private String serverType;

	/**
	 * 服务创建时间
	 */
	private Date createTime;

	/**
	 * 服务更新时间
	 */
	private Date updateTime;

	/**
	 * 超时时间
	 */
	private long timeout;

	public String getMd5() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getServiceName()).append(",");
		sb.append(this.getVersion()).append(",");
		sb.append(this.getNamespace()).append(",");
		sb.append(this.timeout).append(",");
		sb.append(this.serverType).append(",");
		if (this.createTime != null) {
			sb.append(this.createTime.getTime()).append(",");
		}
		if (this.updateTime != null) {
			sb.append(this.updateTime).append(",");
		}

		return DigestUtils.md5Hex(sb.toString());
	}
}
