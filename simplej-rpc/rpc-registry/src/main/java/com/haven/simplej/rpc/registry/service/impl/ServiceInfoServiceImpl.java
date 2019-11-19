package com.haven.simplej.rpc.registry.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.haven.simplej.db.annotation.DataSource;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.model.MethodInfo;
import com.haven.simplej.rpc.model.ServiceInfo;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.model.ServiceListInfo;
import com.haven.simplej.rpc.registry.constant.RpcRegisterConstant;
import com.haven.simplej.rpc.registry.domain.definition.ServiceInfoDefinition;
import com.haven.simplej.rpc.registry.domain.definition.ServiceInstanceRefDefinition;
import com.haven.simplej.rpc.registry.enums.InstanceStatus;
import com.haven.simplej.rpc.registry.enums.ServerType;
import com.haven.simplej.rpc.registry.enums.ServiceStatus;
import com.haven.simplej.rpc.registry.model.InstanceInfoModel;
import com.haven.simplej.rpc.registry.model.ServiceInfoModel;
import com.haven.simplej.rpc.registry.model.ServiceInstanceRefModel;
import com.haven.simplej.rpc.registry.service.InstanceInfoService;
import com.haven.simplej.rpc.registry.service.ServiceInfoService;
import com.haven.simplej.rpc.registry.service.ServiceInstanceRefService;
import com.haven.simplej.security.DigestUtils;
import com.haven.simplej.text.StringUtil;
import com.haven.simplej.time.DateUtils;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import com.haven.simplej.db.base.BaseServiceImpl2;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * rpc服务信息 Service implements
 */
@Slf4j
@Service
@DataSource(dbName = "rpc_register")
public class ServiceInfoServiceImpl extends BaseServiceImpl2<ServiceInfoModel> implements ServiceInfoService {

	@Autowired
	private InstanceInfoService instanceInfoService;
	@Autowired
	private ServiceInstanceRefService instanceRefService;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public boolean register(ServiceInfo service) {
		ServiceInfoModel serviceInfoModel = new ServiceInfoModel();
		serviceInfoModel.setMethodInfo(JSON.toJSONString(service.getMethods()));
		serviceInfoModel.setNamespace(service.getNamespace());
		serviceInfoModel.setServiceName(service.getServiceName());
		serviceInfoModel.setTimeout(service.getTimeout());
		serviceInfoModel.setServiceVersion(service.getVersion());
		serviceInfoModel.setStatus((byte) ServiceStatus.normal.getStatus());
		serviceInfoModel.setCreateTime(DateUtils.getTimestamp(new Date()));
		serviceInfoModel.setUpdateTime(DateUtils.getTimestamp(new Date()));

		ServiceInfoModel serviceReq = new ServiceInfoModel();
		serviceReq.setServiceVersion(service.getVersion());
		serviceReq.setServiceName(service.getServiceName());
		serviceReq.setNamespace(service.getNamespace());
		ServiceInfoModel serviceInfo = this.get(serviceReq);
		if (serviceInfo != null) {
			if (serviceInfo.getStatus().intValue() == ServiceStatus.invalid.getStatus()) {
				serviceInfoModel.setId(serviceInfo.getId());
				int res = this.update(serviceInfoModel);
				log.debug("mysql register update result:{}", res);
			}
		} else {
			try {
				this.save(serviceInfoModel);
			} catch (DuplicateKeyException e) {
				log.info("service info:{} exits", JSON.toJSONString(serviceInfoModel, true));
			}
			serviceInfo = this.get(serviceReq);
		}


		//更新服务实例信息,此次不用batchInsert的原因是允许部分插入失败，比如键重复错误
		if (CollectionUtil.isNotEmpty(service.getInstances())) {
			InstanceInfoModel instance = null;
			for (ServiceInstance serviceInstance : service.getInstances()) {
				instance = instanceInfoService.addInstance(serviceInstance, service.getNamespace());
				if (instance == null) {
					throw new UncheckedException("add instance error");
				}

				ServiceInstanceRefModel instanceRef = new ServiceInstanceRefModel();
				instanceRef.setServiceId(serviceInfo.getId());
				instanceRef.setInstanceId(instance.getId());
				instanceRef.setType(ServerType.tcp.name());
				instanceRef.setStatus((byte) InstanceStatus.normal.getStatus());
				instanceRef.setCreateTime(DateUtils.getTimestamp(new Date()));
				instanceRef.setUpdateTime(DateUtils.getTimestamp(new Date()));
				instanceRefService.add(instanceRef);
			}
		}
		return true;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public boolean unRegister(ServiceInfo service) {
		ServiceInfoModel serviceReq = new ServiceInfoModel();
		serviceReq.setServiceVersion(service.getVersion());
		serviceReq.setServiceName(service.getServiceName());
		serviceReq.setNamespace(service.getNamespace());
		ServiceInfoModel queryModel = this.get(serviceReq);
		if (queryModel != null) {
			queryModel.setStatus((byte) ServiceStatus.invalid.getStatus());
			queryModel.setUpdateTime(DateUtils.getTimestamp(new Date()));
			this.update(queryModel, ServiceInfoDefinition.serviceName.name(), ServiceInfoDefinition.version.name(),
					ServiceInfoDefinition.namespace.name());
			//更新关系记录状态为无效
			ServiceInstanceRefModel instanceRef = new ServiceInstanceRefModel();
			instanceRef.setServiceId(queryModel.getId());
			instanceRef.setType(ServerType.tcp.name());
			instanceRef.setStatus((byte) InstanceStatus.invalid.getStatus());
			instanceRefService.update(instanceRef, ServiceInstanceRefDefinition.serviceId.name());
		} else {
			log.warn("service:{} not exit", JSON.toJSONString(service));
			return false;
		}
		log.info("unRegister service {} success", service);
		return true;
	}

	@Override
	public ServiceListInfo getService(String namespace) {
		ServiceListInfo serviceListInfo = new ServiceListInfo();
		ServiceInfoModel queryModel = new ServiceInfoModel();
		queryModel.setStatus((byte) ServiceStatus.normal.getStatus());
		if (StringUtil.isNotEmpty(namespace)) {
			queryModel.setNamespace(namespace);
		}
		//查询service 信息
		List<ServiceInfoModel> serviceList = this.query(queryModel);
		List<ServiceInfo> serviceInfos = Lists.newArrayList();
		StringBuilder sb = new StringBuilder();
		if (CollectionUtil.isNotEmpty(serviceList)) {
			for (ServiceInfoModel serviceInfoModel : serviceList) {
				if (serviceInfoModel.getStatus().intValue() == ServiceStatus.invalid.getStatus()) {
					log.info("getService service:{} status is invalid,continue", serviceInfoModel);
					continue;
				}
				ServiceInfo service = new ServiceInfo();
				service.setNamespace(serviceInfoModel.getNamespace());
				service.setServiceName(serviceInfoModel.getServiceName());
				service.setTimeout(serviceInfoModel.getTimeout());
				service.setVersion(serviceInfoModel.getServiceVersion());
				service.setCreateTime(serviceInfoModel.getCreateTime());
				service.setUpdateTime(serviceInfoModel.getUpdateTime());
				service.setMethods(Sets.newHashSet(convertMethodInfo(serviceInfoModel.getMethodInfo())));

				//查询实例信息
				service.setInstances(instanceInfoService.getInstance(ServerType.tcp.name(), serviceInfoModel.getId()));
				serviceInfos.add(service);

				//计算md5,目的：判断服务信息和实例信息是否发生变化
				sb.append(service.getMd5());
				service.getInstances().forEach(e -> sb.append(e.getMd5()));
			}
		}
		log.debug("getService response :{}", JSON.toJSONString(serviceInfos, true));
		serviceListInfo.setServiceList(serviceInfos);
		serviceListInfo.setMd5(DigestUtils.md5Hex(sb.toString()));
		return serviceListInfo;
	}

	private List<MethodInfo> convertMethodInfo(String methodInfo) {
		List<MethodInfo> methodInfos = JSON.parseArray(methodInfo, MethodInfo.class);

		return methodInfos;
	}

	@Override
	public boolean heartbeat(ServiceInfo serviceInfo) {
		log.debug("heartbeat serviceInfo:{}", serviceInfo);
		ServiceInfoModel serviceReq = new ServiceInfoModel();
		serviceReq.setServiceVersion(serviceInfo.getVersion());
		serviceReq.setServiceName(serviceInfo.getServiceName());
		serviceReq.setNamespace(serviceInfo.getNamespace());
		ServiceInfoModel queryModel = this.get(serviceReq);
		if (queryModel == null) {
			log.debug("ServiceInfo is empty");
			return true;
		}
		String updateGap = PropertyManager.get(RpcRegisterConstant.RPC_SERVICE_HEARBEAT_TIME_GAP, "5000");
		if (new Date().getTime() - queryModel.getHeartbeatTime() < Integer.parseInt(updateGap)) {
			//5秒内不需要更新
			return true;
		}

		//更新service心跳时间戳
		ServiceInfoModel updateModel = new ServiceInfoModel();
		updateModel.setId(queryModel.getId());
		updateModel.setHeartbeatTime(new Date().getTime());
		this.update(updateModel);
		return true;
	}

}