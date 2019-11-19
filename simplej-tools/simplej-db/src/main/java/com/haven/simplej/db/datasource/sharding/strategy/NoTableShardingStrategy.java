package com.haven.simplej.db.datasource.sharding.strategy;

import com.haven.simplej.db.model.ShardingParameter;

/**
 * @author haven.zhang
 * @date 2019/1/22.
 */
public class NoTableShardingStrategy implements TableShardingStrategy {


	private static final NoTableShardingStrategy INSTANCE = new NoTableShardingStrategy();

	private NoTableShardingStrategy() {
	}
	@Override
	public String getTableName(ShardingParameter parameter) {
		return null;
	}

	public static NoTableShardingStrategy getInstance() {
		return INSTANCE;
	}
}
