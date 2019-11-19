package com.haven.epay.payment.service;

import com.haven.simplej.db.base.BaseService;
import com.haven.epay.payment.model.PaymentListModel;
import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcParam;
import com.haven.simplej.rpc.annotation.RpcService;
import com.haven.simplej.rpc.annotation.RpcMethod;

/**
 * 交易流水表 Service
 */
@RpcService
public interface PaymentListService extends BaseService<PaymentListModel> {

	@RpcMethod(timeout = 200)
	@Doc("查询接口服务")
	String query(@RpcParam @Doc("订单号") String orderId);

	@RpcMethod(timeout = 300)
	@Doc("查询接口服务222")
	String get(@RpcParam @Doc("订单号")  String orderId,@RpcParam @Doc("参数2")int param2);


}