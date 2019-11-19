package com.haven.simplej.db.datasource.sharding.strategy;

import com.haven.simplej.db.model.ShardingKey;
import com.haven.simplej.db.model.ShardingParameter;
import com.haven.simplej.db.model.StrategyKey;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.text.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.zip.CRC32;

/**
 * 通过CRC32算法计算分库编号，适用于固定前缀+编号组成的数据库名，如：user_001
 * 使用方式，在数据库操作类方法加如下注解：
 * @RepositorySharding(strategyBeanName="xxxx" , expression = "new com.haven.etools.db.model.ShardingKey('groupName','user_',16,#user.userCode)")
 * @author haven.zhang
 * @date 2019/1/22.
 */
@Slf4j
public final class CRC32DBShardingStrategy implements DBShardingStrategy {

	private CRC32DBShardingStrategy() {
		log.info("init CRC32DBShardingStrategy");
	}

	private CRC32 crc32 = new CRC32();

	@Override
	public StrategyKey getDataSourceLookupKey(ShardingParameter obj) {
		log.info("Strategy:{} sharding parameter:{}", obj.getName(), obj.getValue());
		ShardingKey shardingKey = (ShardingKey) obj.getValue();
		Object field = shardingKey.getPartitionFieldValue();
		if (field == null) {
			throw new UncheckedException("partitionFieldValue must not be null");
		}
		if (shardingKey.getPartitions() == null) {
			throw new UncheckedException("partitions must not be null");
		}
		if (StringUtil.isEmpty(shardingKey.getDbNamePreffix())) {
			throw new UncheckedException("dbNamePreffix must not be empty");
		}
		int value = 0;

		crc32.update(String.valueOf(field).getBytes());
		value += crc32.getValue();
		crc32.reset();

		int dbNo = value % shardingKey.getPartitions();
		String dbName = shardingKey.getDbNamePreffix() + StringUtils
				.leftPad(String.valueOf(dbNo), shardingKey.getDbNoLength(), "0");
		StrategyKey key = new StrategyKey(dbName, shardingKey.getGroupName());
		return key;
	}
}
