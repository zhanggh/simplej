package com.haven.simplej.rpc.registry.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.haven.simplej.db.base.BaseServiceImpl2;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.rpc.model.UrlInfo;
import com.haven.simplej.rpc.model.ServiceInstance;
import com.haven.simplej.rpc.model.UrlListInfo;
import com.haven.simplej.rpc.registry.constant.RpcRegisterConstant;
import com.haven.simplej.rpc.registry.domain.definition.ServiceInstanceRefDefinition;
import com.haven.simplej.rpc.registry.domain.definition.UrlInfoDefinition;
import com.haven.simplej.rpc.registry.enums.InstanceStatus;
import com.haven.simplej.rpc.registry.enums.ServerType;
import com.haven.simplej.rpc.registry.enums.ServiceStatus;
import com.haven.simplej.rpc.registry.model.InstanceInfoModel;
import com.haven.simplej.rpc.registry.model.ServiceInstanceRefModel;
import com.haven.simplej.rpc.registry.model.UrlInfoModel;
import com.haven.simplej.rpc.registry.service.InstanceInfoService;
import com.haven.simplej.rpc.registry.service.ServiceInstanceRefService;
import com.haven.simplej.rpc.registry.service.UrlInfoService;
import com.haven.simplej.security.DigestUtils;
import com.haven.simplej.text.StringUtil;
import com.haven.simplej.time.DateUtils;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * web服务url信息 Service implements
 */
@Slf4j
@Service
public class UrlInfoServiceImpl extends BaseServiceImpl2<UrlInfoModel> implements UrlInfoService {


	@Autowired
	private InstanceInfoService instanceInfoService;
	@Autowired
	private ServiceInstanceRefService instanceRefService;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public boolean regitster(UrlInfo urlInfo) {
		UrlInfoModel urlInfoModel = new UrlInfoModel();
		urlInfoModel.setNamespace(urlInfo.getNamespace());
		urlInfoModel.setTimeout(urlInfo.getTimeout());
		urlInfoModel.setUri(urlInfo.getUri());
		urlInfoModel.setMethod(urlInfo.getHttpMethod());
		urlInfoModel.setHeader(urlInfo.getHeader());
		urlInfoModel.setUrlVersion(urlInfo.getVersion());
		urlInfoModel.setStatus((byte) ServiceStatus.normal.getStatus());
		urlInfoModel.setCreateTime(DateUtils.getTimestamp(new Date()));
		urlInfoModel.setUpdateTime(DateUtils.getTimestamp(new Date()));
		urlInfoModel.setUrlSchema(urlInfo.getSchema());

		UrlInfoModel serviceReq = new UrlInfoModel();
		serviceReq.setUrlVersion(urlInfo.getVersion());
		serviceReq.setMethod(urlInfo.getHttpMethod());
		serviceReq.setHeader(urlInfo.getHeader());
		serviceReq.setNamespace(urlInfo.getNamespace());
		serviceReq.setUri(urlInfo.getUri());
		UrlInfoModel urlModel = this.get(serviceReq);
		if (urlModel != null) {
			if (urlModel.getStatus().intValue() == ServiceStatus.invalid.getStatus()) {
				urlInfoModel.setId(urlModel.getId());
				int res = this.update(urlInfoModel);
				log.debug("mysql register update result:{}", res);
			}
		} else {
			try {
				this.save(urlInfoModel);
			} catch (DuplicateKeyException e) {
				log.info("service info:{} exits", JSON.toJSONString(urlInfoModel, true));
			}
			urlModel = this.get(serviceReq);
			//@todo 是否需要更新操作
		}


		//更新服务实例信息,此次不用batchInsert的原因是允许部分插入失败，比如键重复错误
		if (CollectionUtil.isNotEmpty(urlInfo.getInstances())) {
			InstanceInfoModel instance = null;
			for (ServiceInstance serviceInstance : urlInfo.getInstances()) {
				instance = instanceInfoService.addInstance(serviceInstance, urlInfo.getNamespace());
				if (instance == null) {
					throw new UncheckedException("add instance error");
				}

				ServiceInstanceRefModel instanceRef = new ServiceInstanceRefModel();
				instanceRef.setServiceId(urlModel.getId());
				instanceRef.setInstanceId(instance.getId());
				instanceRef.setType(ServerType.http.name());
				instanceRef.setStatus((byte) InstanceStatus.normal.getStatus());
				instanceRef.setCreateTime(DateUtils.getTimestamp(new Date()));
				instanceRef.setUpdateTime(DateUtils.getTimestamp(new Date()));
				instanceRefService.add(instanceRef);
			}
		}
		return true;

	}

	@Override
	public boolean unRegitster(UrlInfo urlInfo) {

		UrlInfoModel serviceReq = new UrlInfoModel();
		serviceReq.setUrlVersion(urlInfo.getVersion());
		serviceReq.setUri(urlInfo.getUri());
		serviceReq.setMethod(urlInfo.getHttpMethod());
		serviceReq.setHeader(urlInfo.getHeader());
		serviceReq.setNamespace(urlInfo.getNamespace());
		UrlInfoModel queryModel = this.get(serviceReq);
		if (queryModel != null) {
			queryModel.setStatus((byte) ServiceStatus.invalid.getStatus());
			queryModel.setUpdateTime(DateUtils.getTimestamp(new Date()));
			queryModel.setId(null);
			this.update(queryModel, UrlInfoDefinition.uri.name(), UrlInfoDefinition.method.name(), UrlInfoDefinition.header.name(), UrlInfoDefinition.urlVersion.name(), UrlInfoDefinition.namespace.name());
			//更新关系记录状态为无效
			ServiceInstanceRefModel instanceRef = new ServiceInstanceRefModel();
			instanceRef.setServiceId(queryModel.getId());
			instanceRef.setType(ServerType.http.name());
			instanceRef.setStatus((byte) InstanceStatus.invalid.getStatus());
			instanceRefService.update(instanceRef, ServiceInstanceRefDefinition.serviceId.name(), ServiceInstanceRefDefinition.type.name());
		} else {
			log.warn("service:{} not exit", JSON.toJSONString(urlInfo));
			return false;
		}
		log.info("unRegister service {} success", urlInfo);
		return true;

	}

	@Override
	public UrlListInfo getUrlList(String namespace) {
		UrlInfoModel queryModel = new UrlInfoModel();
		queryModel.setStatus((byte) ServiceStatus.normal.getStatus());
		if (StringUtil.isNotEmpty(namespace)) {
			queryModel.setNamespace(namespace);
		}
		//查询service 信息
		List<UrlInfoModel> urlList = this.query(queryModel);
		List<UrlInfo> urlInfoList = Lists.newArrayList();
		StringBuilder sb = new StringBuilder();
		if (CollectionUtil.isNotEmpty(urlList)) {
			for (UrlInfoModel urlInfoModel : urlList) {
				if (urlInfoModel.getStatus().intValue() == ServiceStatus.invalid.getStatus()) {
					log.info("getService service:{} status is invalid,continue", urlInfoModel);
					continue;
				}
				UrlInfo urlInfo = new UrlInfo();
				urlInfo.setNamespace(urlInfoModel.getNamespace());
				urlInfo.setHeader(urlInfoModel.getHeader());
				urlInfo.setTimeout(urlInfoModel.getTimeout());
				urlInfo.setVersion(urlInfoModel.getUrlVersion());
				urlInfo.setCreateTime(urlInfoModel.getCreateTime());
				urlInfo.setUpdateTime(urlInfoModel.getUpdateTime());

				//查询实例信息
				urlInfo.setInstances(instanceInfoService.getInstance(ServerType.tcp.name(), urlInfoModel.getId()));
				urlInfoList.add(urlInfo);

				//计算md5,目的：判断服务信息和实例信息是否发生变化
				sb.append(urlInfo.getMd5());
				urlInfo.getInstances().forEach(e -> sb.append(e.getMd5()));
			}
		}
		UrlListInfo urlListInfo = new UrlListInfo();
		urlListInfo.setMd5(DigestUtils.md5Hex(sb.toString()));
		urlListInfo.setUrlInfoList(urlInfoList);
		log.debug("getUrlList response :{}", JSON.toJSONString(urlInfoList, true));
		return urlListInfo;
	}

	@Override
	public boolean heartbeat(UrlInfo urlInfo) {
		log.debug("heartbeat urlInfo:{}", JSON.toJSONString(urlInfo));
		UrlInfoModel urlInfoModel = new UrlInfoModel();
		urlInfoModel.setHeader(urlInfo.getHeader());
		urlInfoModel.setMethod(urlInfo.getHttpMethod());
		urlInfoModel.setUri(urlInfo.getUri());
		urlInfoModel.setUrlVersion(urlInfo.getVersion());
		urlInfoModel.setNamespace(urlInfo.getNamespace());
		UrlInfoModel queryModel = this.get(urlInfoModel);
		if (queryModel == null) {
			log.warn("can not find url info :{}", JSON.toJSONString(urlInfo));
			return false;
		}
		int updateGap = PropertyManager.getInt(RpcRegisterConstant.RPC_SERVICE_HEARBEAT_TIME_GAP, 5000);
		if (new Date().getTime() - queryModel.getHeartbeatTime() < updateGap) {
			//5秒内不需要更新
			return true;
		}

		//更新service心跳时间戳
		UrlInfoModel updateModel = new UrlInfoModel();
		updateModel.setId(queryModel.getId());
		updateModel.setHeartbeatTime(new Date().getTime());
		this.update(updateModel);
		return true;
	}
}