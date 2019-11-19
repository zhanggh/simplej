package com.haven.simplej.rpc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * @author haven.zhang
 * @date 2019/2/3.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ServiceLookupKey {
	/**
	 * 命名空间（或者域名），每个应用应该有唯一的一个命名空间
	 */
	private String namespace;
	/**
	 * 服务名
	 */
	private String serviceName;

	/**
	 * 服务版本号
	 */
	private String version;

	/**
	 * 远程方法的唯一id，如果请求包含该字段，则可优先使用该字段进行服务方法查找
	 */
//	private String methodId;






	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ServiceLookupKey lookupKey = (ServiceLookupKey) o;
		return Objects.equals(namespace, lookupKey.namespace) && Objects.equals(serviceName, lookupKey.serviceName) && Objects.equals(version, lookupKey.version) ;
	}

	@Override
	public int hashCode() {
		return Objects.hash(namespace, serviceName, version);
	}
}
