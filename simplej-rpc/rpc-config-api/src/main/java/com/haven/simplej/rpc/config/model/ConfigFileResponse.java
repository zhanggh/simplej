package com.haven.simplej.rpc.config.model;

import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcParam;
import com.haven.simplej.rpc.annotation.RpcStruct;
import lombok.Data;

import java.util.List;

/**
 * 配置文件监听响应模型
 * @author: havenzhang
 * @date: 2018/6/11 23:21
 * @version 1.0
 */
@Data
@RpcStruct
public class ConfigFileResponse {


	@Doc("是否有修改过")
	@RpcParam
	private boolean change;


	@Doc("配置文件集合")
	private List<ConfigFileRpcModel> files;
}
