package com.haven.simplej.db.enums;

import com.haven.simplej.exception.UncheckedException;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据源连接池类型
 * @author haven.zhang
 * @date 2019/1/17.
 */
public enum DatasourceType {

	DRUID,TOMCAT_JDBC,Hikari,BoneCP, DBCP2;

	public static DatasourceType find(String type){
		switch (StringUtils.upperCase(type)){
			case "DRUID":
				return DRUID;
			case "HIKARI":
				return Hikari;
			case "TOMCAT_JDBC":
				return TOMCAT_JDBC;
			case "BONECP":
				return BoneCP;
			case "DBCP2":
				return DBCP2;
		}

		throw new UncheckedException("error dataSourceType");
	}
}
