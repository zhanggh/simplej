package com.haven.simplej.db.model;

/**
 * 分表分库自定义策略的入参
 * @author haven.zhang
 * @date 2019/1/22.
 */
public class ShardingParameter {
	public static final ShardingParameter NO_SHARD = new ShardingParameter();

	/**
	 * sharding strategyBeanName name
	 */
	private String name;
	/**
	 * sharding field value
	 */
	private Object value;

	public ShardingParameter() {
	}

	public ShardingParameter(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
