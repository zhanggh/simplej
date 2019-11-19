package com.haven.simplej.rpc.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;


/**
 * @author haven.zhang
 * @date 2018/6/3.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MethodInfo implements Comparable<MethodInfo>{

	/**
	 * 服务名
	 */
	private String serviceName;
	/**
	 * 方法名
	 */
	private String methodName;
	/**
	 * 返回类型
	 */
	private String returnType;
	/**
	 * methodId ，远程方法的唯一id，生成规则见：
	 * @see com.haven.simplej.rpc.helper.RpcHelper
	 */
	private String methodId;

	/**
	 * 参数类型
	 */
	private String  paramsTypes;

	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 超时时间
	 */
	private long timeout;


	@Override
	public int compareTo(MethodInfo o) {

		return StringUtils.compare(this.methodName,o.methodName);
	}
}
