package com.haven.simplej.rpc.registry.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.haven.simplej.db.annotation.DataSource;
import com.haven.simplej.db.sql.query.Condition;
import com.haven.simplej.db.sql.query.QuerySql;
import com.haven.simplej.db.sql.query.util.TableUtil;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.registry.domain.InstanceInfo;
import com.haven.simplej.rpc.registry.domain.definition.InstanceInfoDefinition;
import com.haven.simplej.rpc.registry.enums.InstanceStatus;
import com.haven.simplej.rpc.registry.model.InstanceInfoModel;
import com.haven.simplej.rpc.registry.service.InstanceInfoService;
import com.haven.simplej.rpc.registry.sql.InstanceSqlBuilder;
import com.haven.simplej.time.DateUtils;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.db.base.BaseServiceImpl2;

import java.util.Date;
import java.util.List;

/**
 * rpc服务的实例信息 Service implements
 */
@Slf4j
@Service
@DataSource(dbName = "rpc_register")
public class InstanceInfoServiceImpl extends BaseServiceImpl2<InstanceInfoModel> implements InstanceInfoService {


	@Override
	public List<InstanceInfoModel> getInstanceGroup(String namespace) {
		InstanceInfo domain = new InstanceInfo();
		QuerySql querySql = new QuerySql(domain.getTableName());

		Condition condition = new Condition();
		condition.addEq(TableUtil.getColumn(domain, InstanceInfoDefinition.namespace.name() ), namespace);
		condition.addGroupBy(TableUtil.getColumn(domain, InstanceInfoDefinition.host.name()));
		condition.addGroupBy(TableUtil.getColumn(domain, InstanceInfoDefinition.port.name()));
		condition.addGroupBy(TableUtil.getColumn(domain, InstanceInfoDefinition.idc.name()));
		querySql.setWhereCondition(condition);
		String sql = querySql.getSqlAndArgs().getLeft();
		List params = querySql.getSqlAndArgs().getRight();
		List ls = this.dao.getJdbcTemplate().query(sql, params.toArray(), new BeanPropertyRowMapper<>(InstanceInfoModel.class));
		return ls;
	}



	@Override
	public boolean heartbeat(ServiceInstance instance) {
		log.debug("heartbeat instance:{}", instance);
		InstanceInfoModel updateModel = new InstanceInfoModel();
		updateModel.setHost(instance.getHost());
		updateModel.setPort(instance.getPort());
		updateModel.setIdc(instance.getIdc());
		updateModel.setRegionId(instance.getRegionId());
		updateModel.setProxyHttpPort(instance.getProxyHttpPort());
		updateModel.setProxyPort(instance.getProxyPort());
		updateModel.setHttpPort(instance.getHttpPort());
		updateModel.setRemark("心跳上报正常");
		updateModel.setNamespace(instance.getNamespace());
		updateModel.setStatus((byte) InstanceStatus.normal.getStatus());
		updateModel.setHeartbeatTime(new Date().getTime());
		int result = this.update(updateModel, InstanceInfoDefinition.host.name(), InstanceInfoDefinition.port.name(), InstanceInfoDefinition.idc.name());
		return result == 1;
	}


	@Override
	public List<ServiceInstance> getInstance(String serverType,long serviceId){
		String sql = InstanceSqlBuilder.getInstanceByServiceId();
		List<InstanceInfoModel> instanceInfoModels = this.dao.getJdbcTemplate().query(sql, new Object[]{
				serverType,
				serviceId}, new BeanPropertyRowMapper<>(InstanceInfoModel.class));
		List<ServiceInstance> instances = Lists.newArrayList();
		if (CollectionUtil.isNotEmpty(instanceInfoModels)) {
			for (InstanceInfoModel instanceInfoModel : instanceInfoModels) {
				ServiceInstance instance = new ServiceInstance();
				instance.setHost(instanceInfoModel.getHost());
				instance.setPort(instanceInfoModel.getPort());
				instance.setProxyPort(instanceInfoModel.getProxyPort());
				instance.setHttpPort(instanceInfoModel.getHttpPort());
				instance.setProxyHttpPort(instanceInfoModel.getProxyHttpPort());
				instance.setIdc(instanceInfoModel.getIdc());
				instance.setRegionId(instanceInfoModel.getRegionId());
				instance.setNamespace(instanceInfoModel.getNamespace());
				instance.setWeight(instanceInfoModel.getWeight().doubleValue());
				instances.add(instance);
			}
		}
		return instances;
	}

	@Override
	/**
	 * 添加实例
	 * @param serviceInstance
	 * @return
	 */
	public InstanceInfoModel addInstance(ServiceInstance serviceInstance, String namespace) {
		InstanceInfoModel instance = new InstanceInfoModel();
		instance.setHost(serviceInstance.getHost());
		instance.setPort(serviceInstance.getPort());
		instance.setProxyHttpPort(serviceInstance.getProxyHttpPort());
		instance.setProxyPort(serviceInstance.getProxyPort());
		instance.setHttpPort(serviceInstance.getHttpPort());
		instance.setIdc(serviceInstance.getIdc());
		instance.setRegionId(serviceInstance.getRegionId());
		instance.setNamespace(namespace);
		instance.setStatus((byte) InstanceStatus.normal.getStatus());
		instance.setCreateTime(DateUtils.getTimestamp(new Date()));
		instance.setUpdateTime(DateUtils.getTimestamp(new Date()));

		//先查询是否存在相同的实例信息
		InstanceInfoModel queryRequest = new InstanceInfoModel();
		queryRequest.setHost(instance.getHost());
		queryRequest.setPort(instance.getPort());
		queryRequest.setIdc(instance.getIdc());
		InstanceInfoModel queryResp = this.get(queryRequest);
		if (queryResp == null) {
			try {
				int ret = this.save(instance);
				log.debug("instance save result:{}", ret == 1);
			} catch (DuplicateKeyException e) {
				log.info("instance info:{} exits", JSON.toJSONString(instance, true));
			}
			queryResp = this.get(queryRequest);
		} else if (queryResp.getStatus().intValue() == InstanceStatus.invalid.getStatus()) {
			queryResp.setStatus((byte) InstanceStatus.normal.getStatus());
			int ret = this.update(queryResp);
			log.debug("instance update result:{}", ret == 1);
		}
		return queryResp;
	}
}