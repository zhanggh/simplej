package com.haven.simplej.rpc.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * 方法参数信息
 * @author: havenzhang
 * @date: 2019/1/10 17:30
 * @version 1.0
 */
@Data
public class ParamMeta {

	/**
	 * 是否为pojo 结构类，1：是，0：否
	 */
	private int structFlag;
	/**
	 * 参数名称
	 */
	private String name;
	/**
	 * 数据类型
	 */
	private String dataType;

	/**
	 * 是否必填
	 */
	private boolean required;

	/**
	 * 最小长度
	 */
	private int minLen;

	/**
	 * 最大长度
	 */
	private int maxLen;

	/**
	 * 参数值正则校验表达式
	 */
	private String regular;
	/**
	 * 参数/字段文档描述
	 */
	private String paramDoc;
	/**
	 * 默认值
	 */
	private String defaultVale;

	/**
	 * 结构体唯一id
	 */
	private String structId;

	/**
	 * 数据类型是否为MAP ，1是，0否
	 */
	private int mapFlag;

	/**
	 * 数据类型是否为list，1是，0否
	 */
	private int listFlag;

	/**
	 * 数据类型是否为数组，1是，0否
	 */
	private int arrayFlag;

	/**
	 * 如果是结构体类的情况下，把结构体类的参数解析到该列表
	 */
	private List<ParamMeta> child = Lists.newArrayList();
}
