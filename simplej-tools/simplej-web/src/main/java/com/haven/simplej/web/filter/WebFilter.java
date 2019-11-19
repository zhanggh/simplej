package com.haven.simplej.web.filter;

import javax.servlet.Filter;

/**
 * @author haven.zhang
 * @date 2019/1/16.
 */
public interface WebFilter extends Filter {

	/**
	 * filter过滤的url
	 * @return
	 */
	public String getUrlMapping();

	/**
	 * 过滤器顺序
	 * @return
	 */
	public int getOrder();
}
