package com.haven.simplej.rpc.auth.model;

import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcStruct;
import lombok.Data;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2018/10/13 18:59
 * @version 1.0
 */
@RpcStruct
@Doc("服务权限信息model")
@Data
public class AuthInfoRpcModel {

	/**
	 * 允许的客户端业务应用的命名空间
	 */
	private String clientNamespace;

	/**
	 * 目标服务列表
	 */
	private List<RpcServerModel> serverModelList;

}
