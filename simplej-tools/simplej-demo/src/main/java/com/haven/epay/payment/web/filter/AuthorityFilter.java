package com.haven.epay.payment.web.filter;

import com.haven.simplej.web.filter.WebFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @Author: havenzhang
 * @Date: 2019/4/5 23:58
 * @Version 1.0
 */
public class AuthorityFilter implements WebFilter {
	@Override
	public String getUrlMapping() {
		return "/**";
	}

	@Override
	public int getOrder() {
		return 1;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		filterChain.doFilter(servletRequest, servletResponse);
	}
}
