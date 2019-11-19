package com.haven.simplej.db.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author haven.zhang
 * @date 2019/1/8.
 */
public class Constant {
	/**
	 * mysql表主键字段
	 */
	public static final String TABLE_PRIMARY_KEY = "id";

	/**
	 * 默认编码
	 */
	public static final String DEFAUL_ENCODE = "utf-8";

	/**
	 * 查询指定库下面的所有表名
	 */
	public static final String QUERY_TABLE_SQL = "SELECT "//
			+ "TABLE_NAME AS tableName, "//
			+ "TABLE_COMMENT AS comment "//
			+ "FROM INFORMATION_SCHEMA.`TABLES` WHERE "//
			+ "TABLE_SCHEMA = ? ";//

	/**
	 * 查询数据库信息
	 */
	public static final String QUERY_SCHEMA_SQL = "SELECT * FROM INFORMATION_SCHEMA.`SCHEMATA` WHERE SCHEMA_NAME=?";

	/**
	 * 数据源配置文件所在目录
	 */
	public static final String DB_PROPERTIE_PATH = "/db";

	/**
	 * 数据源配置文件所在目录
	 */
	public static final String DB_COMMON_PROPERTIE = "/db/common.properties";

	/**
	 * 数据库驱动名
	 */
	public static final String DRIVER_CLASS_NAME = "driverClassName";

	/**
	 * 默认数据库组名,当数据库配置没有指定组名的时候，都归属默认组
	 */
	public static final String DEFAULT_DATA_SOURCE_GROUP_NAME = "defaultGroup";


	/**
	 * 数据库组名properties key
	 */
	public static final String DATA_SOURCE_GROUP_NAME_KEY = "groupName";

	/**
	 * 数据库访问用户
	 */
	public static final String USERNAME = "username";
	/**
	 * 访问密码
	 */
	public static final String PASSWORD = "password";
	/**
	 * 数据库访问ip
	 */
	public static final String DB_HOST = "dbHost";
	/**
	 * 数据库访问端口
	 */
	public static final String DB_PORT = "dbPort";
	/**
	 * 访问schema名（库名）
	 */
	public static final String SCHEMA = "schema";
	/**
	 * 数据库访问的url
	 */
	public static final String URL = "url";
	/**
	 * 初始连接数
	 */
	public static final String INITIAL_SIZE = "initialSize";
	/**
	 * 连接空闲时间
	 */
	public static final String MIN_IDLE = "minIdle";
	/**
	 * 连接空闲时间
	 */
	public static final String MAX_IDLE = "maxIdle";
	/**
	 * 最大连接数
	 */
	public static final String MAX_ACTIVE = "maxActive";
	/**
	 * 最大等待拿连接的时间
	 */
	public static final String MAX_WAIT = "maxWait";
	/**
	 * 检查驱逐无效连接的时间
	 */
	public static final String TIME_BETWEEN_EVICTION_RUNS_MILLIS = "timeBetweenEvictionRunsMillis";
	/**
	 * 驱逐无效连接的时间
	 */
	public static final String MIN_EVICTABLE_IDLE_TIME_MILLIS = "minEvictableIdleTimeMillis";
	/**
	 * 是否在取连接时都进行请求测试连接有效性
	 */
	public static final String TEST_ON_BORROW = "testOnBorrow";
	/**
	 * 是否在归还连接时都进行请求测试连接有效性
	 */
	public static final String TEST_ON_RETURN = "testOnReturn";
	/**
	 * 是否在连接空闲的时候测试连接有效性
	 */
	public static final String TEST_WHILE_IDLE = "testWhileIdle";
	/**
	 * 是否在进行连接的时候测试连接有效性
	 */
	public static final String TEST_ON_CONNECT = "TestOnConnect";
	/**
	 * 连接校验sql
	 */
	public static final String VALIDATION_QUERY = "validationQuery";
	/**
	 * 连接池类型
	 */
	public static final String DATA_SOURCE_TYPE = "dataSourceType";
	/**
	 * 是否为默认数据源标志，默认数据源指，当我们没有显示指定数据源的时候，都使用该数据源
	 */
	public static final String IS_DEFAULT_DATA_SOURCE = "isDefaultDb";
	/**
	 * 数据库服务类型，master 或者 slaver 当我们需要读写分离的时候，写操作会在master服务，读操作落在slaver服务;
	 */
	public static final String DATA_SOURCE_SERVER_IS_MASTER = "isMaster";
	/**
	 * 数据库服务类型，默认为都是主库
	 */
	public static final String DATA_SOURCE_SERVER_MASTER_DEFAULT_FLAG = "true";
	/**
	 * 默认数据库分流权重,当配置没有指定每一个数据源的权重的时候，都默认为此权重值
	 */
	public static final int DEFAULT_DATA_SOURCE_GROUP_WEIGHT = 1;
	/**
	 * 数据库服务分流权重
	 */
	public static final String DATA_SOURCE_SERVER_WEIGHT = "weight";
	/**
	 * 数据库服务编号（可以人为定义一个序号，同一个分组下面不要重复即可，比如001/002/003）
	 */
	public static final String DATA_SOURCE_SERVER_NO = "serverNo";
	/**
	 * 数据源的唯一id
	 */
	public static final String DATA_SOURCE_ID = "dataSourceId";
	/**
	 * 默认的数据库服务编号（可以人为定义一个序号，同一个分组下面不要重复即可，比如001/002/003）
	 */
	public static final String DEFAULT_DATA_SOURCE_SERVER_NO = "001";
	/**
	 * druid连接池数据源类
	 */
	public static final String DATA_SOURCE_DRUID = "com.alibaba.druid.pool.DruidDataSource";
	/**
	 * BONECP连接池数据源类
	 */
	public static final String DATA_SOURCE_BONECP = "com.jolbox.bonecp.BoneCPDataSource";
	/**
	 * hikari连接池数据源类
	 */
	public static final String DATA_SOURCE_HIKARI = "com.zaxxer.hikari.HikariDataSource";
	/**
	 * dbcp2连接池数据源类
	 */
	public static final String DATA_SOURCE_DBCP2 = "org.apache.commons.dbcp2.BasicDataSource";
	/**
	 * tomcat.jdbc连接池数据源类
	 */
	public static final String DATA_SOURCE_TOMCAT_JDBC = "org.apache.tomcat.jdbc.pool.DataSource";
	/**
	 * 数据库查询客户端核心线程池大小
	 */
	public static final String DB_THREAD_POOL_CORE_POOL_SIZE_KEY = "db.threadpool.corePoolSize";
	/**
	 * 数据库查询客户端核心线程池大小 默认大小
	 */
	public static final int DB_THREAD_POOL_CORE_POOL_SIZE_DEFAULT = 16;
	/**
	 * 数据库查询客户端最大线程池大小
	 */
	public static final String DB_THREAD_POOL_MAX_POOL_SIZE_KEY = "db.threadpool.maximumPoolSize";
	/**
	 * 数据库查询客户端最大线程池大小 默认大小
	 */
	public static final int DB_THREAD_POOL_MAX_POOL_SIZE_DEFAULT = 128;
	/**
	 * 数据库查询客户端空闲线程时间
	 */
	public static final String DB_THREAD_KEEPALIVE_TIME_KEY = "db.threadpool.keepAliveTime";
	/**
	 * 数据库查询客户端空闲线程时间 默认大小
	 */
	public static final int DB_THREAD_KEEPALIVE_TIME_DEFAULT = 120;
	/**
	 * 数据库查询客户端工作队列容量时间
	 */
	public static final String DB_THREAD_QUEUE_CAPACITY_KEY = "db.threadpool.workQueue.capacity";
	/**
	 * 数据库查询客户端空闲线程时间 默认大小
	 */
	public static final int DB_THREAD_QUEUE_CAPACITY_DEFAULT = 10000;

	/**
	 * 数据库查询超时时间
	 */
	public static final String DB_THREAD_QUERY_TIMEOUT = "db.thread.query.timeout";

	/**
	 * master 标志
	 */
	public static final String MASTER_FLAG = "master";

	/**
	 * slaver 标志
	 */
	public static final String SLAVER_FLAG = "slaver";

	/**
	 * 写操作的方法关键字
	 */
	public static final List<String> WRITE_FILTERS = Arrays.asList("save", "add", "update", "modify", "create", "delete", "insert");

	/**
	 * schema分隔符
	 */
	public static final String SCHEMA_SPLIT_SYMBOL = ",";
	/**
	 * schema变量占位符
	 */
	public static final String SCHEMA_PARAM_SYMBOL = "${";

	/**
	 * 数据源配置是否起效
	 */
	public static final String DB_ENABLE = "dbEnable";
	/**
	 * 默认名字
	 */
	public static final String DATA_SOURCE_DEFAULT_DB_NAME = "default";

	/**
	 * SQL日志输出文件路径
	 */
	public static final String SQL_LOG_OUTPUT_FILE_KEY = "sql.log.output.file";
	/**
	 * 慢SQL日志
	 */
	public static final String SLOW_SQL_LOG_OUTPUT_FILE_KEY = "sql.slow.log.output.file";

	/**
	 * 默认的SQL 日志文件
	 */
	public static final String SQL_LOG_OUTPUT_FILE_DEFAULT = "logs/sql_log_%s.log";
	/**
	 * 慢SQL
	 */
	public static final String SLOW_SQL_LOG_OUTPUT_FILE_DEFAULT = "logs/slow_sql_log_%s.log";
	/**
	 * 慢SQL耗时阈值
	 */
	public static final long SLOW_SQL_LOG_COST_TIME_DEFAULT = 5;

	/**
	 * 慢SQL 耗时阈值 属性key
	 */
	public static final String SLOW_SQL_LOG_COST_TIME_KEY = "slow.sql.cost.time";


	/**
	 * proxy的HOST
	 */
	public static final String RPC_PROXY_HOST_KEY = "rpc.proxy.host";
	/**
	 * proxy的监听端口号
	 */
	public static final String RPC_PROXY_PORT_KEY = "rpc.proxy.port";

}
