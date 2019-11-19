package com.haven.simplej.rpc.auth.service;

import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcMethod;
import com.haven.simplej.rpc.annotation.RpcParam;
import com.haven.simplej.rpc.annotation.RpcService;
import com.haven.simplej.rpc.auth.model.AuthResponseModel;

/**
 *
 * @author: havenzhang
 * @date: 2018/10/13 18:56
 * @version 1.0
 */
@RpcService(timeout = 1000)
@Doc(value = "rpc服务权限信息管理服务接口", author = "havenzhang", date = "2018/10/13 18:56")
public interface AuthService {


	@RpcMethod(timeout = 30000)
	@Doc("查询某个命名空间下被访问允许的权限列表信息")
	AuthResponseModel queryAuthInfo(@RpcParam(required = true,maxLength = 120) @Doc("服务的命名空间") String namespace);
}
