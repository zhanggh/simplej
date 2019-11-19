package com.haven.simplej.db.conditoin;

import com.haven.simplej.db.constant.Constant;
import com.haven.simplej.db.util.DBUtil;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 判断服务是否包含分库（多个数据源的情况可认为是分库）
 * @author haven.zhang
 * @date 2019/1/27.
 */
public class ShardingDataSourceCondition extends SpringBootCondition implements ApplicationContextAware {
	private ApplicationContext applicationContext;


	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		DBUtil.readDataSourceProp(Constant.DB_PROPERTIE_PATH);
		return new ConditionOutcome(DBUtil.isShardingDataSource(), "sharding flag");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
