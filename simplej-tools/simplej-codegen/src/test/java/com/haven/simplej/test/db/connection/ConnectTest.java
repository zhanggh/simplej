package com.haven.simplej.test.db.connection;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.StartUpApplication;
import com.haven.simplej.db.dao.CommonDao;
import com.haven.simplej.db.manager.DatasourceManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @author: havenzhang
 * @date: 2019/4/25 21:01
 * @version 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartUpApplication.class)
public class ConnectTest {

	@Autowired
	private DatasourceManager manager;


	@Test
	public void connectTest() {
		String url = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&&serverTimezone=UTC&zeroDateTimeBehavior=convertToNull";


		String user = "root";
		String port = "3306";
		String ip = "127.0.0.1";
		String schema = "";
		CommonDao dao = manager.initDao(ip, port, user, "123456", schema);

		List<Map<String, Object>> list = dao.getJdbcTemplate().queryForList("select * from rule_engine.app_info");
		System.out.println(JSON.toJSONString(list, true));

		log.info("-----------------------------------------------------------------------");
		list = dao.getJdbcTemplate().queryForList("select * from authen.menu_info");
		System.out.println(JSON.toJSONString(list, true));
	}
}
