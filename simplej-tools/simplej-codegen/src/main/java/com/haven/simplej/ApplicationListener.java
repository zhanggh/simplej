package com.haven.simplej;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.codegen.model.EntityInfo;
import com.haven.simplej.codegen.service.EntityInfoService;
import com.haven.simplej.db.dao.CommonDao;
import com.haven.simplej.spring.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author haven.zhang
 * @date 2019/1/25.
 */
@Component
@Slf4j
public class ApplicationListener implements org.springframework.context.ApplicationListener<ContextRefreshedEvent> {
	//	@Autowired
	private CommonDao dao;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("--------------------onApplicationEvent--------------------");
//		dao = SpringContext.getBean(CommonDao.class);
		if (event.getApplicationContext().getParent() == null && dao != null) {
			Integer initValue = dao.count("select COUNT(1) from dual", new Object[]{});
			log.info("initValue:{}", initValue);
		}
	}
}
