package com.haven.simplej.rpc.server.http.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.haven.simplej.http.HttpExecuter;
import com.haven.simplej.response.builder.ResponseBuilder;
import com.haven.simplej.response.model.JsonResponse;
import com.haven.simplej.rpc.constant.RpcConstants;
import com.haven.simplej.rpc.server.http.model.HttpModel;
import com.haven.simplej.script.FreeMarkerUtil;
import com.haven.simplej.text.StringUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

/**
 * http 转发工具
 * @author: havenzhang
 * @date: 2018/11/12 19:39
 * @version 1.0
 */
@Controller
@Slf4j
@RequestMapping("/http")
public class HttpController {

	/**
	 * 测试工具模板
	 */
	private static final String TEST_TOOL_TEMPLATE = "http_test.ftl";

	@RequestMapping("/tool")
	public void index(HttpServletResponse response) throws IOException, TemplateException {
		Map<String, Object> data = Maps.newHashMap();
		String resp = FreeMarkerUtil.getText(TEST_TOOL_TEMPLATE, data);
		response.setCharacterEncoding(RpcConstants.DEFAULT_ENCODE);
		response.getWriter().write(resp);
	}

	/**
	 * 纯粹http转发，测试工具使用
	 * @param model 请求参数
	 * @return JsonResponse
	 */
	@ResponseBody
	@RequestMapping("/execute")
	public JsonResponse<String> execute(HttpModel model) {

		HttpExecuter executer = HttpExecuter.create().setEncoding(RpcConstants.DEFAULT_ENCODE).setMimetype(model.getContentType()).build();
		byte[] resp;
		if (HttpMethod.GET.name().equalsIgnoreCase(model.getHttpMethod())) {
			resp = executer.get(model.getUrl(), model.getHttpBody(), parseHeader(model.getHeaders()));
		} else {
			resp = executer.post(model.getUrl(), model.getHttpBody(), parseHeader(model.getHeaders()));
		}
		if (resp == null) {
			return ResponseBuilder.build("null");
		}
		return ResponseBuilder.build(new String(resp));

	}

	/**
	 * 解析头部
	 * @param headerString 头部串
	 * @return Map<String, String>
	 */
	private Map<String, String> parseHeader(String headerString) {
		if (StringUtil.isEmpty(headerString)) {
			return null;
		}
		Map<String, String> headerMap = Maps.newHashMap();
		String[] headers = StringUtil.split(headerString, "\r\n");
		for (String header : headers) {
			int index = header.indexOf(":");
			String name = StringUtil.substring(header, 0, index);
			String value = StringUtil.substring(header, index + 1);
			headerMap.put(name, value.replaceAll("\r\n",""));
		}
		return headerMap;
	}

	@RequestMapping("/test")
	public void test(HttpServletRequest request, HttpServletResponse response) throws IOException {

		StringBuilder sb = new StringBuilder();
		request.getParameterMap().forEach((k, v) -> sb.append("param:" + k + "@" + JSON.toJSONString(v)).append("\n"));
		Enumeration<String> headers = request.getHeaderNames();
		while (headers.hasMoreElements()){
			String name = headers.nextElement();
			sb.append("header:"+name+"@"+ request.getHeader(name)).append("\n");
		}
		sb.append(IOUtils.toString(request.getInputStream()));
		response.getWriter().write(sb.toString());
	}
}
