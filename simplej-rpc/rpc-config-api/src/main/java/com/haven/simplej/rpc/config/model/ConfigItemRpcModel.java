package com.haven.simplej.rpc.config.model;

import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcParam;
import com.haven.simplej.rpc.annotation.RpcStruct;
import lombok.Data;
import lombok.ToString;

/**
 * 具体配置项
 * @author: havenzhang
 * @date: 2019/6/11 23:33
 * @version 1.0
 */
@Data
@ToString
@RpcStruct
public class ConfigItemRpcModel {

	@Doc("唯一id")
	@RpcParam
	private long id;

	@Doc("属性key")
	@RpcParam
	private String key;

	@Doc("命名空间")
	@RpcParam
	private String namespace;

	@Doc("属性值")
	@RpcParam
	private String value;

	@Doc("属性描述")
	@RpcParam
	private String description;

	@Doc("init-初始状态，未生效, valid-有效状态,invalid-无效状态")
	@RpcParam
	private String status;

	/**
	 * 属性版本号
	 */
	@Doc("属性版本号")
	@RpcParam
	private int version;

	@Doc("操作人")
	@RpcParam
	private String operator;

}
