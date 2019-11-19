package com.haven.simplej.rpc.model;

import com.google.common.collect.Lists;
import com.haven.simplej.text.StringUtil;
import lombok.Data;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2019/9/21 21:49
 * @version 1.0
 */
@Data
public class MethodReturn {
	/**
	 * 响应数据类型
	 */
	private String returnType;

	/**
	 * 是否为pojo 结构类，1：是，0：否
	 */
	private int structFlag;

	/**
	 * 响应参数名
	 */
	private String returnParamName;

	/**
	 * model的唯一id
	 */
	private String structId;

	/**
	 * 泛型的信息
	 */
	private List<GenericTypeMeta> genericTypeMetas;

}
