package com.haven.simplej.db.model;

import lombok.Setter;
import lombok.Getter;

/**
 * 数据源lookupkey
 * @author haven.zhang
 * @date 2019/1/23.
 */
@Setter
@Getter
public class DataSourceLookupKey {

	public DataSourceLookupKey(String dbName) {
		super();
		this.dbName = dbName;
	}

	public DataSourceLookupKey(String dbName, boolean masterServer) {
		this.dbName = dbName;
		this.masterServer = masterServer;
	}

	public DataSourceLookupKey(String dbName, String groupName, boolean masterServer) {
		this.dbName = dbName;
		this.groupName = groupName;
		this.masterServer = masterServer;
	}

	public DataSourceLookupKey(String dbName, String groupName, boolean masterServer, String serverNo) {
		this.dbName = dbName;
		this.groupName = groupName;
		this.masterServer = masterServer;
		this.serverNo = serverNo;
	}

	/**
	 * 库名
	 */
	private String dbName;
	/**
	 * 组名，比如主从架构下，同一个业务的表对应的主数据库服务和从数据库服务都是同一个组
	 */
	private String groupName;

	/**
	 * 是否为主服务，false代表从服务（读库）
	 */
	private boolean masterServer;

	/**
	 * 数据库编号（可以在生成key的时候递增生成一个唯一的序号）
	 */
	private String serverNo;

	/**
	 * 流量负载权重，默认都是1，相同
	 */
	private int weight = 1;

	/**
	 * 子组别名，同一个group下面的，相同的dbName的集合，组成一个虚拟的子组，如一个group下有多个不同的DBname,每一个主从架构下的dbName一定是相同的
	 * @return
	 */
	public String getChildGroupName() {
		return groupName + ":" + dbName;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("groupName:").append(groupName);
		str.append(",dbName:").append(dbName);
		str.append(",masterServer:").append(masterServer);
		str.append(",serverNo:").append(serverNo);
		return str.toString();
	}
}
