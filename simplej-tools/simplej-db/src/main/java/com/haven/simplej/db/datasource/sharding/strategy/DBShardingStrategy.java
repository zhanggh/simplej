package com.haven.simplej.db.datasource.sharding.strategy;

import com.haven.simplej.db.model.ShardingParameter;
import com.haven.simplej.db.model.StrategyKey;

/**
 * 分库策略
 * @author haven.zhang
 * @date 2019/1/22.
 */
public interface DBShardingStrategy {

	/**
	 * 获取库名以及分组名
	 * @param parameter
	 * @return
	 */
	public StrategyKey getDataSourceLookupKey(ShardingParameter parameter);
}
