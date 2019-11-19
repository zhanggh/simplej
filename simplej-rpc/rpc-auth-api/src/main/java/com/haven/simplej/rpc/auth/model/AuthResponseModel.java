package com.haven.simplej.rpc.auth.model;

import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcParam;
import com.haven.simplej.rpc.annotation.RpcStruct;
import lombok.Data;

import java.util.List;

/**
 * 只有在权限列表内的客户端与服务端应用才能进行远程调用
 *
 * 1.向服务方管理员申请接入的accessToken
 * 2.
 * @author: havenzhang
 * @date: 2018/10/13 18:59
 * @version 1.0
 */
@RpcStruct
@Doc("服务权限信息列表查询响应model")
@Data
public class AuthResponseModel {

	@RpcParam()
	@Doc("权限信息是否发生变更")
	boolean change;

	@RpcParam
	@Doc("权限列表信息")
	List<AuthInfoRpcModel> authList;
}
