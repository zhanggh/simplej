package com.haven.simplej.db.model.builder;

import com.haven.simplej.db.constant.Constant;
import com.haven.simplej.db.datasource.ReadWriteKeyManager;
import com.haven.simplej.db.model.DataSourceLookupKey;
import com.haven.simplej.db.model.StrategyKey;
import com.haven.simplej.text.StringUtil;

/**
 * @author haven.zhang
 * @date 2019/1/24.
 */
public class DataSourceLookupKeyBuilder {

	public static DataSourceLookupKey build(StrategyKey key,boolean isMaster){
		if(StringUtil.isEmpty(key.getGroupName())){
			key.setGroupName(Constant.DEFAULT_DATA_SOURCE_GROUP_NAME);
		}
		if(!isMaster){
			return ReadWriteKeyManager.getReadKey(key.getGroupName(),key.getDbName());
		}else {
			return ReadWriteKeyManager.getWriteKey(key.getGroupName(),key.getDbName());
		}
	}
}
