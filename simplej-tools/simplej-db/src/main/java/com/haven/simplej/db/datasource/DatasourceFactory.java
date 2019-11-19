package com.haven.simplej.db.datasource;

import com.google.common.collect.Maps;
import com.haven.simplej.bean.BeanUtil;
import com.haven.simplej.db.constant.Constant;
import com.haven.simplej.db.enums.DatasourceType;
import com.haven.simplej.db.model.DataSourceLookupKey;
import com.haven.simplej.db.util.DBUtil;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.sequence.SequenceUtil;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.collection.ArrayUtil;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 连接池工厂类，根据不同的池类型，创建相应的池
 * @author haven.zhang
 * @date 2019/1/17.
 */
@Slf4j
public class DatasourceFactory {

	/**
	 * 默认的连接池
	 */
	private static final String DEFAULT_DATASOURCE_TYPE = "druid";
	//	/**
	//	 * 从库的编号，默认自增生成一个唯一编号
	//	 */
	//	private static AtomicInteger slaverNo = new AtomicInteger(0);
	/**
	 * 连接池缓存
	 */
	private static final Map<Object, Object> dataSourceMap = Maps.newHashMap();

	public static DataSource createDataSource(Properties prop) {
		//用数据库名作为实例名，也作为多数据源路由的key
		String schema = prop.getProperty(Constant.SCHEMA);// schema可以为空,为空时，必须设置为默认的数据源
		String[] dbNames = DBUtil.parserSchemas(schema);
		List<String> dbNameList = new ArrayList<>(ArrayUtil.asList(dbNames));
		String groupName = prop.getProperty(Constant.DATA_SOURCE_GROUP_NAME_KEY, Constant.DEFAULT_DATA_SOURCE_GROUP_NAME);
		boolean isMaster = Boolean.parseBoolean(prop.getProperty(Constant.DATA_SOURCE_SERVER_IS_MASTER, Constant.DATA_SOURCE_SERVER_MASTER_DEFAULT_FLAG));
		int weight = Integer.parseInt(prop.getProperty(Constant.DATA_SOURCE_SERVER_WEIGHT, String.valueOf(Constant.DEFAULT_DATA_SOURCE_GROUP_WEIGHT)));
		String serverNo = prop.getProperty(Constant.DATA_SOURCE_SERVER_NO, Constant.DEFAULT_DATA_SOURCE_SERVER_NO);
		String dataSourceId = prop.getProperty(Constant.DATA_SOURCE_ID, SequenceUtil.generateId());
		//把dataSourceId作为一个虚拟的数据库，目的是方便后面的查找，可以通过DataSourceId取对应的数据源
		dbNameList.add(DBUtil.getDatasourceId(dataSourceId));
		//数据源lookupkey
		DataSourceLookupKey dataSourceLookupKey;
		DataSource dataSource = null;
		if (CollectionUtil.isNotEmpty(dbNameList)) {
			for (String dbName : dbNameList) {
				dataSourceLookupKey = new DataSourceLookupKey(dbName, groupName, isMaster, serverNo);
				if (dataSourceMap.containsKey(dataSourceLookupKey)) {
					log.info("dataSource has create,lookupKey:{}", dataSourceLookupKey);
					//多个db共享一个数据源，可以任意返回一个
					dataSource = (DataSource) dataSourceMap.get(dataSourceLookupKey);
				} else {
					dataSource = null;
				}
			}
			if (dataSource != null) {
				return dataSource;
			}
		}


		//数据库连接池管理器
		String dataSourceType = prop.getProperty(Constant.DATA_SOURCE_TYPE);
		if (StringUtil.isEmpty(dataSourceType)) {
			dataSourceType = DEFAULT_DATASOURCE_TYPE;
		}

		log.info("dataSourceType:{}", dataSourceType);
		if (DatasourceType.DRUID.equals(DatasourceType.find(dataSourceType))) {
			dataSource = getDruidDataSource(prop);
		} else if (DatasourceType.TOMCAT_JDBC.equals(DatasourceType.find(dataSourceType))) {
			dataSource = getTomcatDataSource(prop);
		} else if (DatasourceType.Hikari.equals(DatasourceType.find(dataSourceType))) {
			dataSource = getHikariDataSource(prop);
		} else if (DatasourceType.BoneCP.equals(DatasourceType.find(dataSourceType))) {
			dataSource = getBoneCPDataSource(prop);
		} else if (DatasourceType.DBCP2.equals(DatasourceType.find(dataSourceType))) {
			dataSource = getDbcp2DataSource(prop);
		}
		boolean isDefault = Boolean.parseBoolean(prop.getProperty(Constant.IS_DEFAULT_DATA_SOURCE, "false"));
		if (isDefault || StringUtil.isEmpty(schema)) {
			//默认的数据源，当我们没有显示配置分库策略的时候，默认就是走该数据源做相关数据操作
			String lookupKey = Constant.DATA_SOURCE_DEFAULT_DB_NAME;
			DataSourceLookupKey defaulLookupKey = new DataSourceLookupKey(lookupKey, groupName, isMaster, serverNo);
			dataSourceMap.put(defaulLookupKey, dataSource);
			DataSourceHolder.setDefaultKey(defaulLookupKey);
			if (isMaster) {
				ReadWriteKeyManager.addWriteKey(groupName, defaulLookupKey);
			} else {
				ReadWriteKeyManager.addReadKey(groupName, defaulLookupKey);
			}
		}

		if (CollectionUtil.isNotEmpty(dbNameList)) {
			for (String dbName : dbNameList) {
				if (!isExist(dbName, dataSource)) {
					log.error("dbName:{} is not exist,please check again", dbName);
					throw new UncheckedException(dbName + " is not exist");
				}
				dataSourceLookupKey = new DataSourceLookupKey(dbName, groupName, isMaster, serverNo);
				dataSourceLookupKey.setWeight(weight);
				addDatasource(dataSourceLookupKey,dataSource,isMaster);
			}
		}

		dataSourceLookupKey = new DataSourceLookupKey(DBUtil.getDatasourceId(dataSourceId), Constant.DEFAULT_DATA_SOURCE_GROUP_NAME, isMaster,
				serverNo);
		dataSourceLookupKey.setWeight(weight);
		addDatasource(dataSourceLookupKey,dataSource,isMaster);
		return dataSource;
	}

	private static void addDatasource(DataSourceLookupKey dataSourceLookupKey,DataSource dataSource,boolean isMaster){
		putDataSource(dataSourceLookupKey, dataSource);
		//保存lookupKey到keyManager
		if (isMaster) {
			ReadWriteKeyManager.addWriteKey(dataSourceLookupKey.getGroupName(), dataSourceLookupKey);
		} else {
			ReadWriteKeyManager.addReadKey(dataSourceLookupKey.getGroupName(), dataSourceLookupKey);
		}
	}

	/**
	 * 获取Apache tomcat jdbc pool数据源
	 * @return
	 */
	public static DataSource getTomcatDataSource(Properties prop) {
		//用数据库名作为实例名，也作为多数据源路由的key
		String beanName = prop.getProperty(Constant.SCHEMA);
		Map<String, Object> params = Maps.newHashMap();
		params.put(Constant.VALIDATION_QUERY, prop.get(Constant.VALIDATION_QUERY));
		params.put(Constant.URL, prop.get(Constant.URL));
		params.put(Constant.USERNAME, prop.get(Constant.USERNAME));
		params.put(Constant.PASSWORD, prop.get(Constant.PASSWORD));
		if (StringUtil.isNotEmpty((CharSequence) prop.get(Constant.DRIVER_CLASS_NAME))) {
			params.put(Constant.DRIVER_CLASS_NAME, prop.get(Constant.DRIVER_CLASS_NAME));
		}
		params.put(Constant.INITIAL_SIZE, Integer.parseInt((String) prop.get(Constant.INITIAL_SIZE)));
		params.put(Constant.MIN_IDLE, Integer.parseInt((String) prop.get(Constant.MIN_IDLE)));
		params.put(Constant.MAX_IDLE, Integer.parseInt((String) prop.get(Constant.MAX_IDLE)));
		params.put(Constant.MAX_ACTIVE, Integer.parseInt((String) prop.get(Constant.MAX_ACTIVE)));
		params.put(Constant.TEST_WHILE_IDLE, Boolean.parseBoolean((String) prop.get(Constant.TEST_WHILE_IDLE)));
		params.put(Constant.TEST_ON_BORROW, Boolean.parseBoolean((String) prop.get(Constant.TEST_ON_BORROW)));
		params.put(Constant.TEST_ON_CONNECT, Boolean.parseBoolean((String) prop.get(Constant.TEST_ON_CONNECT)));
		params.put(Constant.TEST_ON_RETURN, Boolean.parseBoolean((String) prop.get(Constant.TEST_ON_RETURN)));
		params.put(Constant.TIME_BETWEEN_EVICTION_RUNS_MILLIS, Integer.parseInt((String) prop.get(Constant.TIME_BETWEEN_EVICTION_RUNS_MILLIS)));
		params.put(Constant.MIN_EVICTABLE_IDLE_TIME_MILLIS, Integer.parseInt((String) prop.get(Constant.MIN_EVICTABLE_IDLE_TIME_MILLIS)));

		Class dsCls = null;
		DataSource dataSource = null;
		try {
			dsCls = Class.forName(Constant.DATA_SOURCE_TOMCAT_JDBC);
			dataSource = newinstance(dsCls, params);
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
		//		SpringContext.registerBean(beanName, dsCls, params);
		return dataSource;
	}

	public static DataSource getHikariDataSource(Properties prop) {
		//用数据库名作为实例名，也作为多数据源路由的key
		String beanName = prop.getProperty(Constant.SCHEMA);
		Map<String, Object> params = Maps.newHashMap();
		params.put("connectionTestQuery", prop.get(Constant.VALIDATION_QUERY));
		params.put("jdbcUrl", prop.get(Constant.URL));
		params.put(Constant.USERNAME, prop.get(Constant.USERNAME));
		params.put(Constant.PASSWORD, prop.get(Constant.PASSWORD));
		if (StringUtil.isNotEmpty((CharSequence) prop.get(Constant.DRIVER_CLASS_NAME))) {
			params.put(Constant.DRIVER_CLASS_NAME, prop.get(Constant.DRIVER_CLASS_NAME));
		}

		params.put("minimumIdle", Integer.parseInt((String) prop.get(Constant.MIN_IDLE)));
		params.put("maximumPoolSize", Integer.parseInt((String) prop.get(Constant.MAX_ACTIVE)));

		Class dsCls = null;
		DataSource dataSource = null;
		try {
			dsCls = Class.forName(Constant.DATA_SOURCE_HIKARI);
			dataSource = newinstance(dsCls, params);
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
		//		SpringContext.registerBean(beanName, dsCls, params);
		return dataSource;
	}

	public static DataSource getDruidDataSource(Properties prop) {
		//用数据库名作为实例名，也作为多数据源路由的key
		String beanName = prop.getProperty(Constant.SCHEMA);
		Map<String, Object> params = Maps.newHashMap();
		params.put(Constant.VALIDATION_QUERY, prop.get(Constant.VALIDATION_QUERY));
		params.put(Constant.URL, prop.get(Constant.URL));
		params.put(Constant.USERNAME, prop.get(Constant.USERNAME));
		params.put(Constant.PASSWORD, prop.get(Constant.PASSWORD));
		if (StringUtil.isNotEmpty((CharSequence) prop.get(Constant.DRIVER_CLASS_NAME))) {
			params.put(Constant.DRIVER_CLASS_NAME, prop.get(Constant.DRIVER_CLASS_NAME));
		}
		params.put(Constant.INITIAL_SIZE, Integer.parseInt((String) prop.get(Constant.INITIAL_SIZE)));
		params.put(Constant.MIN_IDLE, Integer.parseInt((String) prop.get(Constant.MIN_IDLE)));
		params.put(Constant.MAX_IDLE, Integer.parseInt((String) prop.get(Constant.MAX_IDLE)));
		params.put(Constant.MAX_ACTIVE, Integer.parseInt((String) prop.get(Constant.MAX_ACTIVE)));
		params.put(Constant.TEST_WHILE_IDLE, Boolean.parseBoolean((String) prop.get(Constant.TEST_WHILE_IDLE)));
		params.put(Constant.TEST_ON_BORROW, Boolean.parseBoolean((String) prop.get(Constant.TEST_ON_BORROW)));
		params.put(Constant.TEST_ON_RETURN, Boolean.parseBoolean((String) prop.get(Constant.TEST_ON_RETURN)));
		params.put(Constant.TIME_BETWEEN_EVICTION_RUNS_MILLIS, Integer.parseInt((String) prop.get(Constant.TIME_BETWEEN_EVICTION_RUNS_MILLIS)));
		params.put(Constant.MIN_EVICTABLE_IDLE_TIME_MILLIS, Integer.parseInt((String) prop.get(Constant.MIN_EVICTABLE_IDLE_TIME_MILLIS)));

		Class dsCls = null;
		DataSource dataSource = null;
		try {
			dsCls = Class.forName(Constant.DATA_SOURCE_DRUID);
			dataSource = newinstance(dsCls, params);
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
		//		SpringContext.registerBean(beanName, dsCls, params);
		return dataSource;
	}

	public static DataSource getBoneCPDataSource(Properties prop) {
		//用数据库名作为实例名，也作为多数据源路由的key
		String beanName = prop.getProperty(Constant.SCHEMA);
		Map<String, Object> params = Maps.newHashMap();
		params.put("initSQL", prop.get(Constant.VALIDATION_QUERY));
		params.put("jdbcUrl", prop.get(Constant.URL));
		params.put(Constant.USERNAME, prop.get(Constant.USERNAME));
		params.put(Constant.PASSWORD, prop.get(Constant.PASSWORD));
		if (StringUtil.isNotEmpty((CharSequence) prop.get(Constant.DRIVER_CLASS_NAME))) {
			params.put(Constant.DRIVER_CLASS_NAME, prop.get(Constant.DRIVER_CLASS_NAME));
		}
		params.put("minConnectionsPerPartition", Integer.parseInt((String) prop.get(Constant.MIN_IDLE)));
		params.put("maxConnectionsPerPartition", Integer.parseInt((String) prop.get(Constant.MAX_ACTIVE)));
		params.put("idleConnectionTestPeriodInSeconds", Integer.parseInt((String) prop.get(Constant.MIN_EVICTABLE_IDLE_TIME_MILLIS)));

		Class dsCls = null;
		DataSource dataSource = null;
		try {
			dsCls = Class.forName(Constant.DATA_SOURCE_BONECP);
			dataSource = newinstance(dsCls, params);
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
		//		SpringContext.registerBean(beanName, dsCls, params);
		return dataSource;
	}


	public static DataSource getDbcp2DataSource(Properties prop) {
		//用数据库名作为实例名，也作为多数据源路由的key
		String beanName = prop.getProperty(Constant.SCHEMA);
		Map<String, Object> params = Maps.newHashMap();
		params.put(Constant.VALIDATION_QUERY, prop.get(Constant.VALIDATION_QUERY));
		params.put(Constant.URL, prop.get(Constant.URL));
		params.put(Constant.USERNAME, prop.get(Constant.USERNAME));
		params.put(Constant.PASSWORD, prop.get(Constant.PASSWORD));
		if (StringUtil.isNotEmpty((CharSequence) prop.get(Constant.DRIVER_CLASS_NAME))) {
			params.put(Constant.DRIVER_CLASS_NAME, prop.get(Constant.DRIVER_CLASS_NAME));
		}
		params.put(Constant.INITIAL_SIZE, Integer.parseInt((String) prop.get(Constant.INITIAL_SIZE)));
		params.put(Constant.MIN_IDLE, Integer.parseInt((String) prop.get(Constant.MIN_IDLE)));
		params.put(Constant.MAX_IDLE, Integer.parseInt((String) prop.get(Constant.MAX_IDLE)));
		params.put("maxTotal", Integer.parseInt((String) prop.get(Constant.MAX_ACTIVE)));
		params.put(Constant.TEST_WHILE_IDLE, Boolean.parseBoolean((String) prop.get(Constant.TEST_WHILE_IDLE)));
		params.put(Constant.TEST_ON_BORROW, Boolean.parseBoolean((String) prop.get(Constant.TEST_ON_BORROW)));
		params.put(Constant.TEST_ON_RETURN, Boolean.parseBoolean((String) prop.get(Constant.TEST_ON_RETURN)));
		params.put(Constant.TIME_BETWEEN_EVICTION_RUNS_MILLIS, Integer.parseInt((String) prop.get(Constant.TIME_BETWEEN_EVICTION_RUNS_MILLIS)));
		params.put(Constant.MIN_EVICTABLE_IDLE_TIME_MILLIS, Integer.parseInt((String) prop.get(Constant.MIN_EVICTABLE_IDLE_TIME_MILLIS)));

		Class dsCls = null;
		DataSource dataSource = null;
		try {
			dsCls = Class.forName(Constant.DATA_SOURCE_DBCP2);
			dataSource = newinstance(dsCls, params);
		} catch (Exception e) {
			throw new UncheckedException(e);
		}
		//		SpringContext.registerBean(beanName, dsCls, params);
		return dataSource;
	}

	/**
	 * 创建实例
	 * @param clz
	 * @param params
	 * @param <T>
	 * @return
	 */
	private static <T> T newinstance(Class clz, Map params) throws Exception {
		T obj = null;
		obj = (T) BeanUtil.copyFromMap(params, clz, false);
		return obj;
	}

	private static void putDataSource(DataSourceLookupKey dataSourceLookupKey, DataSource dataSource) {
		dataSourceMap.put(dataSourceLookupKey, dataSource);
	}

	public static Map<Object, Object> getDataSourceMap() {
		return dataSourceMap;
	}

	/**
	 * 判断某个schema是否存在
	 * @param schemaName 库名
	 * @param dataSource 数据源
	 * @return boolean
	 */
	public static boolean isExist(String schemaName, DataSource dataSource) {
		if(StringUtil.startsWith(schemaName,Constant.DATA_SOURCE_ID)){
			return true;
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(Constant.QUERY_SCHEMA_SQL, schemaName);
		return CollectionUtil.isNotEmpty(list);
	}
}
