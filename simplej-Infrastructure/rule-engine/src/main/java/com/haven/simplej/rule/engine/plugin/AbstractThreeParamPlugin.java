package com.haven.simplej.rule.engine.plugin;


/**
 * 插件抽象类
 * 支持3个入参的插件
 * @Author: havenzhang
 * @Date: 2019/4/18 19:33
 * @Version 1.0
 */
public abstract class AbstractThreeParamPlugin<T, P1, P2, P3> {

	/**
	 * 插件执行入口
	 * @param request1 请求参数1
	 * @param request2 请求参数2
	 * @param request3 请求参数3
	 * @return 返回
	 */
	public abstract <T> T execute(P1 request1, P2 request2,P3 request3);
}
