package com.haven.simplej.codegen.strategy;

import com.haven.simplej.db.annotation.Strategy;
import com.haven.simplej.db.datasource.sharding.strategy.TableShardingStrategy;
import com.haven.simplej.db.model.ShardingParameter;

/**
 * @author haven.zhang
 * @date 2019/1/28.
 */
@Strategy("tableStrategy")
public class CodegenTableShardingStrategy implements TableShardingStrategy {
	@Override
	public String getTableName(ShardingParameter parameter) {
		return (String) parameter.getValue();
	}
}
