package com.haven.simplej.rpc.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * rpcStruct 元信息
 * @author: havenzhang
 * @date: 2019/9/21 17:07
 * @version 1.0
 */
@Data
public class RpcModelMeta {

	/**
	 * 模型名称
	 */
	private String modelName;

	/**
	 * 文档描述，如该model的使用场景
	 */
	private String modelDoc;

	/**
	 * model 唯一的id
	 */
	private String modelId;

	/**
	 * model代码创建的时间
	 */
	private String createTime;

	/**
	 * 作者
	 */
	private String author;

	private List<ParamMeta> paramMetaList = Lists.newArrayList();
}
