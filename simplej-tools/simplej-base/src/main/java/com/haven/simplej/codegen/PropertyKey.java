package com.haven.simplej.codegen;

/**
 * 所有properties文件定义的属性名，环境变量名均需要在该字典类定义
 * 属性常量命名规则：properties文件名_属性
 * @author haven.zhang
 * @date 2018/12/27.
 */
public final class PropertyKey {
	/**
	 * 代码生成器连接的db用户名
	 */
	public static final String DB_USER = "username";
	/**
	 * 代码生成器连接的db 的host
	 */
	public static final String DB_HOST = "dbHost";
	/**
	 * 代码生成器连接的db 的PORT
	 */
	public static final String DB_PORT = "dbPort";
	/**
	 * 代码生成器连接的db 用户密码
	 */
	public static final String DB_PASSWORD = "password";
	/**
	 * 代码生成器连接的db 库名
	 */
	public static final String SCHEMA = "schema";
	/**
	 * 代码生成器连接的db 库名
	 */
	public static final String DB_URL = "url";
	/**
	 * 代码生成mybatis mapper文件的目录
	 */
	public static final String MAPPERS_PATH = "mappers.path";
	/**
	 * 代码生成java类文件的目录
	 */
	public static final String JAVA_OUTPUT_FILE = "java.file.path";

	/**
	 * 项目基础包名
	 */
	public static final String BASE_PACKAGE = "base.package";
	/**
	 * 项目业务名
	 */
	public static final String BUSINESS_NAME = "business.name";
	/**
	 * 数据库表对应的schema
	 */
	public static final String TABLE_SCHEMA = "table.schema";
	/**
	 * 数据库表对应名，多个表时，value用英文逗号分隔
	 */
	public static final String TABLE_NAMES = "table.names";
	/**
	 * config.properties的app.name属性
	 */
	public static final String CONFIG_APP_NAME = "app.name";
	/**
	 * 环境变量
	 */
	public static final String ENV_PROFILE = "env.profile";
	/**
	 * 项目类型，普通java app项目还是web项目
	 */
	public static final String PROJECT_TYPE = "project.type";
	/**
	 * 项目描述
	 */
	public static final String PROJECT_DESCRIPT = "project.descript";
	/**
	 * 生成项目的目录
	 */
	public static final String PROJECT_PATH = "project.path";
	/**
	 * 生成项目的groupid
	 */
	public static final String PROJECT_GROUPID = "project.groupId";

	/**
	 * 项目名称英文
	 */
	public static final String PROJECT_ARTIFACTID = "project.artifactId";
	/**
	 * 生成项目的版本号
	 */
	public static final String PROJECT_VERSION = "project.version";


	/**
	 * 生成项目的父级groupid
	 */
	public static final String PROJECT_PARENT_GROUPID = "project.parent.groupId";

	/**
	 * 项目名称父级artifactId
	 */
	public static final String PROJECT_PARENT_ARTIFACTID = "project.parent.artifactId";
	/**
	 * 生成项目的父级版本号
	 */
	public static final String PROJECT_PARENT_VERSION = "project.parent.version";
	/**
	 * 数据操作dao实现方式，mybatis 或者 原生的jdbcTemplate封装的CommonDao
	 */
	public static final String PROJECT_DAO_TYPE = "project.dao.type";
	/**
	 * 数据源类型，是否分库，single：单库，sharding:分库
	 */
	public static final String PROJECT_DATASOURCE_TYPE = "project.datasource.type";
	/**
	 * 数据源配置是否起效
	 */
	public static final String DB_ENABLE = "dbEnable";

}
