package com.haven.simplej.codegen;


import lombok.Getter;
import lombok.Setter;

/**
 * @author haven.zhang
 * @date 2019/1/10.
 */
@Setter
@Getter
public class ProjectInfo implements Cloneable{

	private String webArtifactId;
	private String webGroupId;
	private String webVersion;

	private String serviceArtifactId;
	private String serviceGroupId;
	private String serviceVersion;

	private String parentArtifactId;
	private String parentGroupId;
	private String parentVersion;
	/**
	 * 工程类型：simpleApp,webApp
	 */
	private String projectType;
	/**
	 * 工程简介
	 */
	private String projectDesc;
	/**
	 * 工程根目录
	 */
	private String rootPath;
	/**
	 * 公共包目录
	 */
	private String basePackage;
	/**
	 * 业务英文名
	 */
	private String businessName;
	/**
	 * 工程使用的dao类型，可选：mybatis / commonDao
	 */
	private String projectDaoType;
	/**
	 * 服务名
	 */
	private String appName;
	/**
	 * 生成代码时，指定的表名，多个表以英文逗号分隔
	 */
	private String tableNames;
	/**
	 * 生成代码时，指定的数据库名
	 */
	private String tableSchema;
	/**
	 * 单独生成java代码时输出的目录，如生成service、domain代码
	 */
	private String javaFilePath;
	/**
	 * 单独生成mybatis mapper配置时输出的目录
	 */
	private String mappersPath;
	/**
	 * 数据源类型，是否分库，single：单库，sharding:分库
	 * 默认是单库
	 */
	private String datasourceType = "single";

	/**
	 * 数据库ip
	 */
	private String dbHost;
	/**
	 * 数据库port
	 */
	private String port;
	/**
	 * 数据库访问密码
	 */
	private String password;
	/**
	 * 数据库访问密码
	 */
	private String userName;
}
