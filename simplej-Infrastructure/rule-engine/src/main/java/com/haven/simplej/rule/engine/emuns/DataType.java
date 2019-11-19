package com.haven.simplej.rule.engine.emuns;

import com.haven.simplej.rule.engine.exception.RuleException;

/**
 * 数据类型
 * @author: havenzhang
 * @date: 2019/4/18 19:43
 * @version 1.0
 */
public enum DataType {
	INT("java.lang.Integer"),
	LONG("java.lang.Long"),
	DOUBLE("java.lang.Double"),
	FLOAT("java.lang.Float"),
	CHARACTER("java.lang.Character"),
	BOOLEAN("java.lang.Boolean"),
	SHORT("java.lang.Short"),
	STRING("java.lang.String"),
	STRING_BUFFER("java.lang.StringBuffer"),
	STRING_BUILDER("java.lang.StringBuilder"),
	VOID("java.lang.Void"),
	DATE("java.util.Date"),
	TIMESTAMP("java.sql.Timestamp"),
	BYTE("java.lang.Byte");

	String value;
	DataType(String value){
		this.value = value;
	}

	/**
	 * 获取数据类型
	 * @param dataType
	 * @return
	 */
	public static Class findType(DataType dataType){

		Class type;
		try {
			type = Class.forName(dataType.value);
		} catch (ClassNotFoundException e) {
			throw new RuleException(e, RuleError.data_type_parser_error);
		}
		return type;
	}

}
