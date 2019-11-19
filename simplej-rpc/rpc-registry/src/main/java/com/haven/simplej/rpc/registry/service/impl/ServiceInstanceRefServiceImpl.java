package com.haven.simplej.rpc.registry.service.impl;

import com.haven.simplej.rpc.registry.sql.InstanceSqlBuilder;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.rpc.registry.model.ServiceInstanceRefModel;
import com.haven.simplej.rpc.registry.service.ServiceInstanceRefService;
import com.haven.simplej.db.base.BaseServiceImpl2;

import java.util.List;

/**
 * 服务与实例的关联关系 Service implements
 */
@Slf4j
@Service
public class ServiceInstanceRefServiceImpl extends BaseServiceImpl2<ServiceInstanceRefModel>
		implements ServiceInstanceRefService {


	@Override
	public boolean add(ServiceInstanceRefModel model) {

		String sql = InstanceSqlBuilder.queryInstanceRefSql();
		List<ServiceInstanceRefModel> refModelList = this.dao.getJdbcTemplate().query(sql, new Object[]{
				model.getInstanceId(),
				model.getServiceId()}, new BeanPropertyRowMapper<>(ServiceInstanceRefModel.class));
		if(CollectionUtil.isNotEmpty(refModelList)){
			return true;
		}
		this.save(model);
		return false;
	}
}