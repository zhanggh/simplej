package com.haven.simplej.codegen.strategy;

import com.haven.simplej.db.annotation.Strategy;
import com.haven.simplej.db.datasource.sharding.strategy.DBShardingStrategy;
import com.haven.simplej.db.model.ShardingParameter;
import com.haven.simplej.db.model.StrategyKey;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认的数据源策略，比如单库的时候
 * @author haven.zhang
 * @date 2019/1/22.
 */
@Slf4j
@Strategy("codegenDBShardingStrategy")
public final class CodegenDBShardingStrategy implements DBShardingStrategy {


	private CodegenDBShardingStrategy() {
		log.info("init CodegenDBShardingStrategy");
	}

	@Override
	public StrategyKey getDataSourceLookupKey(ShardingParameter obj) {
		log.info("Strategy:{} sharding parameter:{}", obj.getName(), obj.getValue());
		StrategyKey key = new StrategyKey(null);
		return key;
	}

}
