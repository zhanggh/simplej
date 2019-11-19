package com.haven.simplej.test.annotations;

import com.haven.simplej.db.annotation.DBShardingStrategy;
import com.haven.simplej.db.annotation.DataSource;

/**
 * @Author: havenzhang
 * @Date: 2019/4/2 9:45
 * @Version 1.0
 */
@DataSource(dbName = "test")
public class DBShardingService {

	public static void main(String[] args) {
		Class  clz = DBShardingService.class;
		DataSource ann = (DataSource) clz.getAnnotation(DataSource.class);

		System.out.println(ann.dbName());
	}
}
