package com.haven.simplej.limiter;

import com.haven.simplej.limiter.enums.LimiterType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by haven.zhang on 2019/1/3.
 */

@Setter
@Getter
public class LimiterConfig {
	/**
	 * 最大总流量qps，所有节点总和
	 */
	private int maxTotalQps;

	/**
	 * 单节点最大qps
	 */
	private int maxSigleQps;

	/**
	 * 流控器的类型
	 */
	private LimiterType type;

	/**
	 * 流控器别名
	 */
	private String alias;

	public String toString(){
		return new StringBuilder().append("maxTotalQps:").append(maxTotalQps)
				.append(",maxSigleQps:").append(maxSigleQps)
				.append(",type:").append(type)
				.append(",alias:").append(alias).toString();
	}
}