package com.haven.simplej.db.model;

import com.haven.simplej.db.constant.Constant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分库策略自定义的key
 * @author haven.zhang
 * @date 2019/1/24.
 */
@Setter
@Getter
@ToString
public class StrategyKey {
	/**
	 * 库名
	 */
	private String dbName;
	/**
	 * 组名，比如主从架构下，同一个业务的表对应的主数据库服务和从数据库服务都是同一个组
	 */
	private String groupName = Constant.DEFAULT_DATA_SOURCE_GROUP_NAME;


	public StrategyKey(){
		super();
	}

	public StrategyKey(String dbName) {
		this.dbName = dbName;
	}

	public StrategyKey(String dbName, String groupName) {
		this.dbName = dbName;
		this.groupName = groupName;
	}

}
