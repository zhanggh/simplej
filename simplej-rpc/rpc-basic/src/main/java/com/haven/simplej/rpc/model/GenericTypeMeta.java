package com.haven.simplej.rpc.model;

import lombok.Data;

import java.lang.reflect.Type;

/**
 * 类型的泛型元信息
 * @author: havenzhang
 * @date: 2018/9/22 21:39
 * @version 1.0
 */
@Data
public class GenericTypeMeta {

	/**
	 * 返回类型的泛型是否为pojo 结构类，1：是，0：否
	 */
	private int structFlag;

	/**
	 * 泛型
	 */
	private Type genericType;
	/**
	 * 泛型名称
	 */
	private String genericTypeName;

	/**
	 * model的唯一id
	 */
	private String structId;

}
