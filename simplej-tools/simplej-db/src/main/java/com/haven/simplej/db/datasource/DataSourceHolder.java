package com.haven.simplej.db.datasource;

import com.haven.simplej.db.constant.Constant;
import com.haven.simplej.db.model.DataSourceLookupKey;
import com.haven.simplej.db.model.StrategyKey;
import com.haven.simplej.db.model.builder.DataSourceLookupKeyBuilder;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.text.StringUtil;

import javax.sql.DataSource;

/**
 * 当前线程数据源key管理器
 * @author haven.zhang
 * @date 2019/1/22.
 */
public class DataSourceHolder {

	/**
	 * 默认的数据源lookupkey，只能有一个默认的数据源
	 */
	private static DataSourceLookupKey defaultKey;
	private static ThreadLocal<DataSourceLookupKey> dataSourceLookupKey = new ThreadLocal<DataSourceLookupKey>();
	private static ThreadLocal<String> currentDbName = new ThreadLocal<>();
	private static ThreadLocal<String> currentTableName = new ThreadLocal<>();

	public static void putKey(DataSourceLookupKey lookupKey) {
		dataSourceLookupKey.set(lookupKey);
	}

	public static DataSourceLookupKey getKey() {
		return dataSourceLookupKey.get();
	}

	public static void clear() {
		dataSourceLookupKey.remove();
		currentDbName.remove();
		currentTableName.remove();
	}

	public static DataSourceLookupKey getDefaultKey() {

		return defaultKey;
	}

	public static void setDefaultKey(DataSourceLookupKey key) {
		defaultKey = key;
	}

	/**
	 * 获取当前数据库操作的数据库名
	 * @return
	 */
	public static String getCurrentDbName() {

		return currentDbName.get();
	}

	/**
	 * 获取当前线程操作的表名
	 * @return
	 */
	public static String getCurrenTableName(){
		return currentTableName.get();
	}

	public static void setCurrentTableName(String tableName){
		currentTableName.set(tableName);
	}
	/**
	 * 设置当前线程执行的数据库名
	 * @param dbName
	 */
	public static void setCurrentDbName(String dbName) {
		currentDbName.set(dbName);
	}

	public static DataSource getDataSource() {
		return getDataSource(getCurrentDbName());
	}

	public static DataSource getDataSource(String dbName) {
		if(StringUtil.isEmpty(dbName)){
			throw new UncheckedException("dbName can not be empty");
		}
		return getDataSource(Constant.DEFAULT_DATA_SOURCE_GROUP_NAME, dbName, true);
	}

	/**
	 * 根据库名获取数据源
	 * @param dbName 库名
	 * @param isMaster 是否为主库
	 * @return datasource
	 */
	public static DataSource getDataSource(String groupName, String dbName, boolean isMaster) {

		StrategyKey key = new StrategyKey(dbName, groupName);
		DataSourceLookupKey lookupKey = DataSourceLookupKeyBuilder.build(key, isMaster);

		return (DataSource) DatasourceFactory.getDataSourceMap().get(lookupKey);
	}
}
