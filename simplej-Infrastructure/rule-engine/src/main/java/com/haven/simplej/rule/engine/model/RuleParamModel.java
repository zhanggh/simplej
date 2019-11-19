package com.haven.simplej.rule.engine.model;

import com.haven.simplej.rule.engine.emuns.ParamType;
import lombok.Data;

/**
 * @Author: havenzhang
 * @Date: 2019/4/18 15:53
 * @Version 1.0
 */
@Data
public class RuleParamModel implements Comparable<RuleParamModel>{

	/**
	 * 参数名
	 */
	private String name;
	/**
	 * 参数值，可以是常量，也可以是变量，如：$Payment.trxAmt，$plugin.10.User.name
	 */
	private String value;

	/**
	 * 参数值类型
	 */
	private String valueType;

	/**
	 * 参数类型，比如：constant，normal_variable、plugin_result
	 */
	private ParamType paramType;

	private int order;

	@Override
	public int compareTo(RuleParamModel o) {
		return Integer.compare(order,o.order);
	}
}
