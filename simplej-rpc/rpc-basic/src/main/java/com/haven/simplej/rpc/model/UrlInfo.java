package com.haven.simplej.rpc.model;

import com.google.common.collect.Lists;
import com.haven.simplej.security.DigestUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: havenzhang
 * @date: 2018/5/19 11:00
 * @version 1.0
 */
@Data
public class UrlInfo extends UrlLookupKey {


	private String contentType;
	/**
	 * http/https
	 */
	private String schema;

	/**
	 * 超时时间
	 */
	private long timeout;


	/**
	 * 服务创建时间
	 */
	private Date createTime;

	/**
	 * 服务更新时间
	 */
	private Date updateTime;

	/**
	 * 实例
	 */
	private List<ServiceInstance> instances = Lists.newArrayList();

	public String getMd5() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getContentType()).append(",");
		sb.append(this.getVersion()).append(",");
		sb.append(this.getNamespace()).append(",");
		sb.append(this.timeout).append(",");
		sb.append(this.getHttpMethod()).append(",");
		sb.append(this.getUri()).append(",");
		sb.append(this.getSchema()).append(",");
		sb.append(this.getTimeout()).append(",");
		sb.append(this.getHeader()).append(",");
		if (this.createTime != null) {
			sb.append(this.createTime.getTime()).append(",");
		}
		if (this.updateTime != null) {
			sb.append(this.updateTime).append(",");
		}

		return DigestUtils.md5Hex(sb.toString());
	}
}
