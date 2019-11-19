package com.haven.simplej.rpc.server.http.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.haven.simplej.response.builder.ResponseBuilder;
import com.haven.simplej.response.enums.RespCode;
import com.haven.simplej.response.model.JsonResponse;
import com.haven.simplej.rpc.constant.HttpField;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.model.MethodMeta;
import com.haven.simplej.rpc.server.helper.ServiceInfoHelper;
import com.haven.simplej.rpc.server.http.helper.RpcTestCaseHelper;
import com.haven.simplej.rpc.server.http.model.Http2RpcModel;
import com.haven.simplej.rpc.model.ServiceMeta;
import com.haven.simplej.rpc.server.http.model.TestCaseModel;
import com.haven.simplej.rpc.server.http.service.RpcHttpApiService;
import com.haven.simplej.script.FreeMarkerUtil;
import com.haven.simplej.sequence.SequenceUtil;
import com.haven.simplej.text.StringUtil;
import com.vip.vjtools.vjkit.base.ExceptionUtil;
import com.vip.vjtools.vjkit.base.type.Pair;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * rpc 服务 自助测试控制类，为当前rpc应用服务下的所有rpcservice提供一个简单的web测试端界面
 * @author: havenzhang
 * @date: 2018/6/10 15:54
 * @version 1.0
 */
@Controller
@RequestMapping("/rpc")
@Slf4j
public class RpcApiController {


	/**
	 * rpc 测试工具模板
	 */
	private static final String TEST_TOOL_TEMPLATE = "rpc_api_test.ftl";

	@Autowired
	private RpcHttpApiService service;

	/**
	 * http://localhost:port/rpc/test
	 * 进入自助测试界面
	 */
	@RequestMapping("/tool")
	public void index(HttpServletResponse response) throws IOException, TemplateException {

		List<ServiceMeta> serviceList = service.query();
		Map<String, Object> data = Maps.newHashMap();
		data.put("serviceList", serviceList);
		String resp = FreeMarkerUtil.getText(TEST_TOOL_TEMPLATE, data);
		response.setCharacterEncoding(RpcConstants.DEFAULT_ENCODE);
		response.getWriter().write(resp);
	}


	@RequestMapping("/query/methods")
	public void queryMethods(String serviceName, HttpServletResponse response) throws IOException, TemplateException {
		Map<String, Object> data = Maps.newHashMap();
		data.put("methods", service.queryMethods(serviceName));
		String resp = FreeMarkerUtil.getText("method_list.ftl", data);
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(resp);
	}


	@RequestMapping("/query/params")
	public void queryParams(String methodId, HttpServletResponse response) throws IOException, TemplateException {
		Map<String, Object> data = Maps.newHashMap();
		data.put("paramList", service.queryParams(methodId));
		String resp = FreeMarkerUtil.getText("param_list.ftl", data);
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(resp);
	}

	/**
	 * 优先使用header的参数
	 * @param request
	 * @param model
	 */
	private void copyHeader(HttpServletRequest request, Http2RpcModel model) {
		if (StringUtil.isNotEmpty(request.getHeader(HttpField.methodId))) {
			model.setMethodId(request.getHeader(HttpField.methodId));
		}
		if (StringUtil.isNotEmpty(request.getHeader(HttpField.methodName))) {
			model.setMethodName(request.getHeader(HttpField.methodName));
		}
		if (StringUtil.isNotEmpty(request.getHeader(HttpField.serviceName))) {
			model.setServiceName(request.getHeader(HttpField.serviceName));
		}
		if (StringUtil.isNotEmpty(request.getHeader(HttpField.serviceVersion))) {
			model.setServiceVersion(request.getHeader(HttpField.serviceVersion));
		}
		if (StringUtil.isNotEmpty(request.getHeader(HttpField.serviceVersion))) {
			model.setMethodParamTypes(request.getHeader(HttpField.methodParamTypes));
		}
	}

	/**
	 * 执行rpc方法
	 */
	@ResponseBody
	@RequestMapping("/execute")
	public JsonResponse<Object> rpcRequest(Http2RpcModel model, HttpServletRequest request) {
		Object resp;
		Class serviceClz;
		try {
			SequenceUtil.putTraceId(request.getHeader(RpcConstants.TRACE_ID));
			copyHeader(request, model);
			log.debug("rpcRequest model:{}", JSON.toJSONString(model));
			//执行方法实例
			Method method;
			if (StringUtil.isNotEmpty(model.getMethodId())) {
				Pair<Class, Method> serviceAndMethod = ServiceInfoHelper.getServiceAndMethod(model.getMethodId());
				method = serviceAndMethod.getRight();
				serviceClz = serviceAndMethod.getLeft();
			} else if (StringUtil.isNotEmpty(model.getMethodName()) && StringUtil.isNotEmpty(model.getMethodParamTypes())) {
				serviceClz = ServiceInfoHelper.getServiceClsss(model.getServiceName());
				if (serviceClz == null) {
					return ResponseBuilder.build(model.getServiceName() + " class can not found");
				}
				method = ServiceInfoHelper.getMethod(model.getServiceName(), model.getMethodName(),
						model.getMethodParamTypes());
			} else {
				log.warn("invalid request,request header:{}", JSON.toJSONString(request.getHeaderNames()));
				return ResponseBuilder.build(RespCode.FAIL.name(), "invalid request");
			}

			// 执行方法
			resp = ServiceInfoHelper.invokeMethod(serviceClz, method, request);
		} catch (Exception e) {
			log.error("rpcRequest error", e);
			resp = ExceptionUtil.stackTraceText(e);
		}
		return ResponseBuilder.buildResponse(JSON.toJSONString(resp, true), resp);
	}

	/**
	 * 方法信息查询
	 */
	@ResponseBody
	@RequestMapping("/method/info")
	public JsonResponse<MethodMeta> getMethodInfo(String methodId, HttpServletRequest request) throws IOException {

		MethodMeta methodMeta = ServiceInfoHelper.getMethodMeta(methodId);
		log.debug("methodId:{} methodMeta:{}", methodId, JSON.toJSONString(methodMeta, true));
		return ResponseBuilder.build(methodMeta);
	}
}
