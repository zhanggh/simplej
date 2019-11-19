package com.haven.simplej.db.test.sharding;

import com.haven.simplej.db.constant.Constant;
import com.haven.simplej.db.datasource.sharding.strategy.DBShardingStrategy;
import com.haven.simplej.db.model.ShardingParameter;
import com.haven.simplej.db.model.StrategyKey;
import org.apache.commons.lang3.StringUtils;

import java.util.zip.CRC32;

/**
 * @author: havenzhang
 * @date: 2019/9/5 22:42
 * @version 1.0
 */
public class ShardingWithNameStrategy implements DBShardingStrategy {

	private CRC32 crc32 = new CRC32();


	@Override
	public StrategyKey getDataSourceLookupKey(ShardingParameter parameter) {
		int value = 0;
		crc32.update(String.valueOf(parameter.getValue()).getBytes());
		value += crc32.getValue();
		crc32.reset();
		int mod = value % 6;
		if(mod == 0){
			mod = 1;
		}
		String dbName = "test" + StringUtils.leftPad(String.valueOf(mod), 2, "0");
		StrategyKey key = new StrategyKey(dbName, Constant.DEFAULT_DATA_SOURCE_GROUP_NAME);
		return key;
	}
}
