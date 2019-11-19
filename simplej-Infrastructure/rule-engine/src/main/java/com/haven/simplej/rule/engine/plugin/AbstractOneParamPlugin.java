package com.haven.simplej.rule.engine.plugin;


/**
 * 只有一个入参的插件
 * 插件抽象类
 * @Author: havenzhang
 * @Date: 2019/4/18 19:33
 * @Version 1.0
 */
public abstract class AbstractOneParamPlugin<T, P> {

	/**
	 * 插件执行入口
	 * @param request 请求参数池
	 * @return 返回
	 */
	public abstract <T> T execute(P request);
}
