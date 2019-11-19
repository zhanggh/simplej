package com.haven.simplej.rpc.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * 方法信息
 * @author: havenzhang
 * @date: 2019/1/10 17:29
 * @version 1.0
 */
@Data
public class MethodMeta {

	/**
	 * 方法名称
	 */
	private String methodName;

	/**
	 * rpc 远程方法的唯一id，
	 * 除了ServiceLookupKey可以查找某个service信息之外，也可以通过methodId进行查询
	 * rpc服务在启动的时候，会为每一个rpcMethod生成一个唯一的id
	 */
	private String methodId;

	/**
	 * 方法文档描述
	 */
	private String methodDoc;

	/**
	 * 方法参数信息
	 */
	private List<ParamMeta>  params= Lists.newArrayList();

	/**
	 * 方法超时时间
	 */
	private long timeout;

	/**
	 * 方法返回参数
	 */
	private MethodReturn methodReturn;

	/**
	 * model代码创建的时间
	 */
	private String createTime;

	/**
	 * 作者
	 */
	private String author;
}
