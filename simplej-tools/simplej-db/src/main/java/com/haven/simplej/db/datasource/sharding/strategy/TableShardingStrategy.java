package com.haven.simplej.db.datasource.sharding.strategy;

import com.haven.simplej.db.model.ShardingParameter;

/**
 * 分表策略
 * @author haven.zhang
 * @date 2019/1/22.
 */
public interface TableShardingStrategy {

	/**
	 * 获取表名
	 * @param parameter
	 * @return
	 */
	public String getTableName(ShardingParameter parameter);
}
