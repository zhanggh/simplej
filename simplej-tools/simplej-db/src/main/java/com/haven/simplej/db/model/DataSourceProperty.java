package com.haven.simplej.db.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据源连接配置属性
 * @author haven.zhang
 * @date 2019/1/15.
 */
@Setter
@Getter
@ToString
public class DataSourceProperty {

	/**
	 * 数据库驱动名
	 */
	private String driverClassName;

	/**
	 * 数据库访问用户
	 */
	private String username;
	/**
	 * 访问密码
	 */
	private String password;
	/**
	 * 数据库访问ip
	 */
	private String dbHost;
	/**
	 * 数据库访问端口
	 */
	private String dbPort;
	/**
	 * 访问schema名（库名）
	 */
	private String schema;
	/**
	 * 数据库访问的url
	 */
	private String url;
	/**
	 * 初始连接数
	 */
	private int initialSize;
	/**
	 * 连接空闲时间
	 */
	private int minIdle;
	/**
	 * 最大连接数
	 */
	private int maxActive;
	/**
	 * 最大等待拿连接的时间
	 */
	private int maxWait;
	/**
	 * 检查驱逐无效连接的时间
	 */
	private int timeBetweenEvictionRunsMillis = 90000;
	/**
	 * 驱逐无效连接的时间
	 */
	private int minEvictableIdleTimeMillis = 1800000;
	/**
	 * 是否在取连接时都进行请求测试连接有效性
	 */
	private boolean testOnBorrow = false;
	/**
	 * 是否在归还连接时都进行请求测试连接有效性
	 */
	private boolean testOnReturn = false;
	/**
	 * 是否在连接空闲的时候测试连接有效性
	 */
	private boolean testWhileIdle = true;
}
