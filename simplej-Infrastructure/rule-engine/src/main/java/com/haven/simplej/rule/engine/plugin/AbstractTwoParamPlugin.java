package com.haven.simplej.rule.engine.plugin;


/**
 * 插件抽象类
 * 支持两个入参的插件
 * @Author: havenzhang
 * @Date: 2019/4/18 19:33
 * @Version 1.0
 */
public abstract class AbstractTwoParamPlugin<T, P1, P2> {

	/**
	 * 插件执行入口
	 * @param request1 请求参数1
	 * @param request2 请求参数2
	 * @return 返回
	 */
	public abstract <T> T execute(P1 request1, P2 request2);
}
