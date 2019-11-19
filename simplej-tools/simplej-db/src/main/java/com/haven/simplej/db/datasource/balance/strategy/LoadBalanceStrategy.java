package com.haven.simplej.db.datasource.balance.strategy;

/**
 * 数据源负载均衡接口
 * 比如一主多从的架构，读操作的时候，需要做负载
 * @author haven.zhang
 * @date 2019/1/22.
 */
public interface LoadBalanceStrategy<T> {

	T elect();

	void removeTarget(T t);

	void recoverTarget(T t);
}
