package com.haven.simplej.rpc.transaction;

/**
 * 分布式事务管理器接口
 * @author: havenzhang
 * @date: 2018/6/29 23:33
 * @version 1.0
 */
public interface TransactionManager {
	long start();

	void end();

	void rollback();

	int getStatus();
}
