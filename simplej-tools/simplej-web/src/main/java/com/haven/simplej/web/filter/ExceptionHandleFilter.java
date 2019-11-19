package com.haven.simplej.web.filter;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.haven.simplej.exception.SimplejException;
import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.io.FileUtil;
import com.haven.simplej.response.builder.ResponseBuilder;
import com.haven.simplej.response.enums.RespCode;
import com.haven.simplej.response.model.JsonResponse;
import com.haven.simplej.script.VelocityUtil;
import com.haven.simplej.web.constant.WebConstant;
import com.haven.simplej.web.helper.WebHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 主要作用，异常处理
 * @author haven.zhang
 * @date 2019/1/9.
 */
@Slf4j
public class ExceptionHandleFilter implements WebFilter {


	/**
	 * 过滤器拦截的url
	 */
	private String urlMapping;

	public ExceptionHandleFilter(String url) {
		this.urlMapping = url;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		try {
			filterChain.doFilter(servletRequest, servletResponse);
		} catch (Exception e) {
			if(e instanceof IOException){
				return;
			}
			String traceId = MDC.get(WebConstant.TRACE_ID);
			StringBuilder errorMsg = new StringBuilder("traceId:");
			errorMsg.append(traceId);
			errorMsg.append("  ");
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			log.error("url:{} request error", request.getRequestURL(), e);
			JsonResponse resp = null;
			if (e instanceof SimplejException) {
				errorMsg.append(e.getMessage());
			} else {
				errorMsg.append("process fail,please contact system manager");
			}
			resp = ResponseBuilder.build(RespCode.FAIL.name(), errorMsg.toString());
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			if (WebHelper.isAjaxRequest(request) || WebHelper.isJsonRequest(request)) {
				log.info("request from ajax");
				response.setHeader("Content-Type", "application/json;charset=UTF-8");
				response.getOutputStream().print(JSON.toJSONString(resp));
			} else {
				String template = FileUtil.getResourceStr("/template/error_template.html");
				Map<String, Object> params = Maps.newHashMap();
				params.put("respMsg", errorMsg.toString());
				String msg = VelocityUtil.getText(template, params);
				response.getOutputStream().print(msg);
			}
		}

	}

	@Override
	public String getUrlMapping() {
		return this.urlMapping;
	}

	@Override
	public int getOrder() {
		return 2;
	}
}
