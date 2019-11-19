package com.haven.simplej.test.spring;

import com.google.common.collect.Maps;
import com.haven.simplej.StartUpApplication;
import com.haven.simplej.db.dao.CommonDao;
import com.haven.simplej.sequence.SequenceUtil;
import com.haven.simplej.spring.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author: havenzhang
 * @date: 2019/4/25 11:54
 * @version 1.0
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartUpApplication.class)
public class SpringTest {


	@Test
	public void registerBeanTest() {
		Map<String, Object> prop = Maps.newHashMap();
//		prop.put("jdbcTemplate", jdbcTemplate);
		SpringContext.registerBean("commonDao" + SequenceUtil.generateId(), CommonDao.class, prop);
		CommonDao dao = SpringContext.getBean(CommonDao.class);
		System.out.println(dao);
	}
}
