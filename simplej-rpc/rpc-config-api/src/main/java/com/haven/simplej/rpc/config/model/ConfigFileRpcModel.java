package com.haven.simplej.rpc.config.model;

import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcParam;
import com.haven.simplej.rpc.annotation.RpcStruct;
import lombok.Data;

/**
 * @author: havenzhang
 * @date: 2018/6/11 23:25
 * @version 1.0
 */
@Data
@RpcStruct
public class ConfigFileRpcModel {

	@Doc("版本号")
	private String version;

	@Doc("当前版本的配置文件内容")
	private String fileContext;

	@Doc("配置文件名")
	private String fileName;

	@Doc("唯一编号")
	private long id;

	@Doc("init-初始状态，未生效, valid-有效状态,invalid-无效状态")
	@RpcParam
	private String status;

	@Doc("命名空间")
	@RpcParam
	private String namespace;
}
