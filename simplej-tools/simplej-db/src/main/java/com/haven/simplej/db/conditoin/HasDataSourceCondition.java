package com.haven.simplej.db.conditoin;

import com.haven.simplej.db.constant.Constant;
import com.haven.simplej.db.util.DBUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.List;
import java.util.Properties;

/**
 * 判断服务启动的时候是否有数据源配置
 * @author: havenzhang
 * @date: 2018/4/24 23:01
 * @version 1.0
 */
@Slf4j
public class HasDataSourceCondition extends SpringBootCondition implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		List<Properties> datasourceConfs = DBUtil.readDataSourceProp(Constant.DB_PROPERTIE_PATH);
		log.info("HasDataSourceCondition result:{}", datasourceConfs.size());
		return new ConditionOutcome(datasourceConfs.size() != 0, "no datasource flag");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
