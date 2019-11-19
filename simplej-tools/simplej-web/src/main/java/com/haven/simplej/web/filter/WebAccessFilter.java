package com.haven.simplej.web.filter;

import com.haven.simplej.sequence.SequenceUtil;
import com.haven.simplej.web.constant.WebConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.*;
import java.io.IOException;

/**
 * 主要作用，管理每一个请求的traceId
 * @author haven.zhang
 * @date 2019/1/9.
 */
@Slf4j
public class WebAccessFilter implements WebFilter {



	/**
	 * 过滤器拦截的url
	 */
	private String urlMapping;

	public WebAccessFilter(String url) {
		this.urlMapping = url;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		MDC.put(WebConstant.TRACE_ID, SequenceUtil.generateId());
		log.debug("before access");
		filterChain.doFilter(servletRequest, servletResponse);
		log.debug("after access");
		MDC.remove(WebConstant.TRACE_ID);
	}

	@Override
	public String getUrlMapping() {
		return this.urlMapping;
	}

	@Override
	public int getOrder() {
		return 1;
	}
}
