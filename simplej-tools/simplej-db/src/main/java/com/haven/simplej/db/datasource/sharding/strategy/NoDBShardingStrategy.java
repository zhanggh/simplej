package com.haven.simplej.db.datasource.sharding.strategy;

import com.haven.simplej.db.model.ShardingParameter;
import com.haven.simplej.db.model.StrategyKey;

/**
 * 默认的数据源策略，比如单库的时候
 * @author haven.zhang
 * @date 2019/1/22.
 */
public final class NoDBShardingStrategy implements DBShardingStrategy {
	private static final NoDBShardingStrategy INSTANCE = new NoDBShardingStrategy();

	private NoDBShardingStrategy() {
	}

	@Override
	public StrategyKey getDataSourceLookupKey(ShardingParameter obj) {

		return null;
	}

	public static NoDBShardingStrategy getInstance() {
		return INSTANCE;
	}
}
