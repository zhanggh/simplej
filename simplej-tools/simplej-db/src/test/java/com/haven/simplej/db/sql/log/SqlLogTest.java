package com.haven.simplej.db.sql.log;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: havenzhang
 * @date: 2019/9/11 19:50
 * @version 1.0
 */
public class SqlLogTest {

	public static void main(String[] args) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		String sql = "select * from userinfo";
		for (int i=0;i<10909999;i++)
			SlowQuerySqlLogger.log(sql,new String[]{"test"},random.nextInt(1,500));

		System.out.println("---------------");
	}
}
