package com.haven.simplej.db.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * db数据类型
 * @author  haven.zhang
 * @date 2019/1/7.
 */
public enum  DataType {
	VARCHAR,
	BOOLEAN,
	TINYINT,
	SMALLINT,
	INT,
	BIGINT,
	REAL,
	DOUBLE,
	NUMERIC,
	DECIMAL,
	DATE,
	DATETIME,
	TIMESTAMP,
	CHAR,
	BINARY,
	TINYTEXT,
	TEXT,
	MEDIUMTEXT,
	LONGTEXT,
	BLOB;

	public String getValue(){
		return StringUtils.lowerCase(this.name());
	}
}
