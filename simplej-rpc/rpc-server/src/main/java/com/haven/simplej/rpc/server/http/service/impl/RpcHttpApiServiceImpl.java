package com.haven.simplej.rpc.server.http.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.haven.simplej.rpc.model.MethodMeta;
import com.haven.simplej.rpc.model.ParamMeta;
import com.haven.simplej.rpc.server.helper.ServiceInfoHelper;
import com.haven.simplej.rpc.model.ServiceMeta;
import com.haven.simplej.rpc.server.http.service.RpcHttpApiService;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author: havenzhang
 * @date: 2019/9/10 17:34
 * @version 1.0
 */
@Slf4j
@Service
public class RpcHttpApiServiceImpl implements RpcHttpApiService {




	@Override
	public List<ServiceMeta> query() {

		if (CollectionUtil.isNotEmpty(ServiceInfoHelper.getServiceMetaList())) {
			log.info("serviceDataList:{} ", JSON.toJSONString(ServiceInfoHelper.getServiceMetaList(), true));
			return ServiceInfoHelper.getServiceMetaList();
		}
		return Lists.newArrayList();
	}

	@Override
	public List<MethodMeta> queryMethods(String serviceName) {

		return ServiceInfoHelper.getMethodDataList(serviceName);
	}

	@Override
	public Method getMethod(String methodId) {

		return ServiceInfoHelper.getMethod(methodId);
	}

	@Override
	public List<ParamMeta> queryParams(String methodId) {

		return ServiceInfoHelper.getMethodParams(methodId);
	}

}
