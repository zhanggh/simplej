package com.haven.simplej.rpc.filter.impl;

import com.alibaba.fastjson.JSON;
import com.haven.simplej.rpc.enums.FilterOrder;
import com.haven.simplej.rpc.filter.FilterChain;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.helper.RpcHelper;
import com.haven.simplej.rpc.model.RpcHeader;
import com.haven.simplej.rpc.model.RpcRequest;
import com.haven.simplej.rpc.model.RpcResponse;
import com.haven.simplej.rpc.validate.RpcParamValidator;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.logging.PerformanceUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;

/**
 * rpc请求参数校验
 * @author: havenzhang
 * @date: 2018/5/26 20:34
 * @version 1.0
 */
@Slf4j
public class RpcParamValidateFilter implements RpcFilter {
	@Override
	public void doFilter(RpcRequest request, RpcResponse response, FilterChain chain) {

		if (validate(request)) {
			//只有校验通过才可以继续执行
			chain.doFilter(request, response);
		} else {
			log.debug("response validate fail,request:{}", JSON.toJSONString(request, true));
		}

	}

	/**
	 * 基本参数校验
	 * @param request 请求
	 * @return 校验结果
	 */
	private boolean validate(RpcRequest request) {

		long start = System.currentTimeMillis();
		//头部参数校验
		RpcHeader header = request.getHeader();
		RpcParamValidator.headerValid(header);

		//报文体参数校验
		List body = (List) request.getBody();
		Method method;
		String methodId = header.getMethodId();
		if (StringUtil.isEmpty(methodId)) {
			methodId = RpcHelper.getMethodId(header.getServiceName(), header.getMethodName(),
					header.getMethodParamTypes());
		}

		method = RpcHelper.getMethod(methodId);
		RpcParamValidator.validate(method, body.toArray());
		log.debug("validate cost:{}", System.currentTimeMillis() - start);
		return true;
	}

	@Override
	public int getOrder() {
		return FilterOrder.PARAM_VALIDATE_FILTER.order();
	}
}
