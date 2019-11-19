package com.haven.simplej.db.model;

import com.haven.simplej.db.constant.Constant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分库策略的key
 * @author haven.zhang
 * @date 2019/1/26.
 */
@Setter
@Getter
@ToString
public class ShardingKey {
	/**
	 * 数据库分组名，在db/xxx.propertie文件中配置，如果没有配置则默认为defaultGroup
	 */
	private String groupName = Constant.DEFAULT_DATA_SOURCE_GROUP_NAME;
	/**
	 * dbName前缀，如：user_001库的前缀是user_
	 */
	private String dbNamePreffix;

	/**
	 * 分区字段，比如用户库使用用户号作为分区（分表）字段，则partionField是user_code
	 */
	private Object partitionFieldValue;

	/**
	 * 分区数量
	 */
	private Integer partitions;

	/**
	 * 数据库名编号长度，不足前补零，比如 dbNoLength为3， 计算的dbNo等于1，补零后为001
	 */
	private int dbNoLength = 3;

	public ShardingKey(String dbNamePrefix,Integer partitions, Object partitionFieldValue) {
		this.dbNamePreffix =dbNamePrefix;
		this.partitionFieldValue =partitionFieldValue;
		this.partitions = partitions;
	}

	public ShardingKey(String dbNamePrefix,Integer partitions, Object partitionFieldValue,int dbNoLength) {
		this.dbNamePreffix =dbNamePrefix;
		this.partitionFieldValue =partitionFieldValue;
		this.partitions = partitions;
		this.dbNoLength = dbNoLength;
	}

	public ShardingKey(String groupName, String dbNamePrefix,Integer partitions, Object partitionFieldValue) {
		this.groupName =groupName;
		this.dbNamePreffix =dbNamePrefix;
		this.partitionFieldValue =partitionFieldValue;
		this.partitions = partitions;
	}

	public ShardingKey(String groupName, String dbNamePrefix,Integer partitions, Object partitionFieldValue,int dbNoLength) {
		this.groupName =groupName;
		this.dbNamePreffix =dbNamePrefix;
		this.partitionFieldValue =partitionFieldValue;
		this.partitions = partitions;
		this.dbNoLength = dbNoLength;
	}
}
