package com.haven.simplej.rpc.proxy.http;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.haven.simplej.rpc.constant.HttpField;
import com.haven.simplej.rpc.proxy.http.dispatch.HttpDispatcher;
import com.haven.simplej.rpc.proxy.http.model.HttpRequest;
import com.haven.simplej.rpc.proxy.http.model.HttpResponse;
import com.haven.simplej.sequence.SequenceUtil;
import com.haven.simplej.text.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * http协议转发
 * @Author: havenzhang
 * @Date: 2018/5/10 16:41
 * @Version 1.0
 */
@Slf4j
@Controller
public class HttpProxyController {

	@Autowired
	private HttpDispatcher dispatcher;

	/**
	 * 接受所有http请求
	 * @param request request
	 * @param response response
	 * @throws IOException
	 */
	@RequestMapping("/**")
	public void dispatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SequenceUtil.putTraceId(request.getHeader(HttpField.msgId));
		log.debug("request:{}", JSON.toJSONString(request.getParameterMap(), true));
		doService(request, response);
		log.debug("finish dispatch success");
	}

	/**
	 * http 转发
	 * @param request req
	 * @param response resp
	 */
	private void doService(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.debug("request url:{} method:{} contentType:{},queryString{}", request.getRequestURI(),
				request.getMethod(), request.getContentType(), request.getQueryString());
		Map<String, String> reqParams = Maps.newHashMap();
		request.getParameterMap().forEach((k, v) -> {
			StringBuilder valueStr = new StringBuilder();
			for (String s : v) {
				valueStr.append(s).append(",");
			}
			reqParams.put(k, valueStr.substring(0, valueStr.length() - 1));
		});

		reqParams.putAll(parserQueryString(request.getQueryString()));
		byte[] stream = IOUtils.toByteArray(request.getInputStream());

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setReqParams(reqParams);
		httpRequest.setHttpHeader(getHeader(request));
		httpRequest.setStream(stream);
		httpRequest.setContentType(request.getContentType());
		httpRequest.setHttpMethod(request.getMethod());
		String destUrl = request.getParameter(HttpField.destUrl);
		if (StringUtil.isNotEmpty(destUrl)) {
			httpRequest.setHttpUrl(destUrl);
		} else {
			httpRequest.setHttpUrl(request.getRequestURI());
		}
		HttpResponse httpResponse = dispatcher.dispatch(httpRequest);
		response.getOutputStream().write(httpResponse.getBody());
		httpResponse.getHeader().forEach((k, v) -> response.addHeader(k, v));
		response.getWriter().flush();
	}

	/**
	 * 获取http 请求报文头部
	 * @param request http 请求
	 * @return Map
	 */
	private Map<String, String> getHeader(HttpServletRequest request) {
		Map<String, String> header = new HashMap<>();
		while (request.getHeaderNames().hasMoreElements()) {
			String name = request.getHeaderNames().nextElement();
			header.put(name, request.getHeader(name));
		}
		return header;
	}


	/**
	 * 解包get请求的queryString
	 * @param queryStr
	 * @return
	 */
	private Map<String, String> parserQueryString(String queryStr) {

		String[] params = StringUtil.split(queryStr, "&");
		Map<String, String> reqParams = new HashMap<>(params.length);
		for (String s : params) {
			String[] keyValue = StringUtil.split(s, "=");
			reqParams.put(keyValue[0], keyValue[1]);
		}
		return reqParams;
	}

}
