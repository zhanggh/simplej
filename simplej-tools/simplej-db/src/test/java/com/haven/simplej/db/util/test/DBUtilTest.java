package com.haven.simplej.db.util.test;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.db.util.DBUtil;
import org.junit.Test;

/**
 * @author: havenzhang
 * @date: 2019/9/4 13:15
 * @version 1.0
 */
public class DBUtilTest {


	@Test
	public void test1(){
		String schemas = "hkpay,test_${00}...${99}@2";

		System.out.println(JSON.toJSONString(DBUtil.parserSchemas(schemas)));
	}


	@Test
	public void test2(){
		String dbName = "test01";
		String sql1= String.format("select * from  %s.userinfo",dbName);
		System.out.println(sql1);
	}
}
