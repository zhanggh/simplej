package com.haven.simplej.rpc.mock.service;

import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcMethod;
import com.haven.simplej.rpc.annotation.RpcService;
import com.haven.simplej.rpc.mock.model.MockInfo;
import com.haven.simplej.rpc.mock.model.MockMethod;


/**
 * 挡板业务接口
 * @author: havenzhang
 * @date: 2018/9/27 19:39
 * @version 1.0
 */
@Doc("接口挡板数据管理")
@RpcService(timeout = 1000)
public interface MockService  extends BaseRpcService{


	@Doc("查询所有被设置挡板的远程服务方法信息")
	@RpcMethod(timeout = 15000)
	MockInfo getMockInfo();

	@Doc("添加挡板信息")
	@RpcMethod(timeout = 15000)
	int addMockMethod(MockMethod mockMethod);

	@Doc("修改挡板信息")
	@RpcMethod(timeout = 15000)
	int updateMockMethod(MockMethod mockMethod);

	@Doc("获取挡板信息")
	@RpcMethod(timeout = 15000)
	int getMockMethod(MockMethod mockMethod);

	@Doc("删除挡板信息")
	@RpcMethod(timeout = 15000)
	int deleteMockMethod(MockMethod mockMethod);
}
