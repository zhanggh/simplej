package com.haven.simplej.codegen.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author haven.zhang
 * @date 2019/1/16.
 */
@Setter
@Getter
@ToString
public class ProjectRequestModel {

	private String artifactId;
	private String groupId;
	private String version = "1.0-SNAPSHOT";
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
	private String rootPath = "/output/project";
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
	private String daoType;
	/**
	 * 数据源是否分库
	 */
	private String datasourceType;


	/**
	 * 项目依赖的表
	 */
	private String tables;


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

	/**
	 * 表名数组
	 */
	private String[] tabNames;
	/**
	 * 是否使用数据库
	 */
	private int useDbFlag;
	/**
	 * 是否集成RPC框架
	 */
	private int useRpcFlag;

	/**
	 * 是否需要生成管理平台界面，1-生成，0-不需要生成
	 */
	private int needAdminTemplate;

}
