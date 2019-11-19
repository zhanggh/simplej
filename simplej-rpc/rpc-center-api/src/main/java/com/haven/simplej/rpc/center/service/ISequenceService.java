package com.haven.simplej.rpc.center.service;

import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcMethod;
import com.haven.simplej.rpc.annotation.RpcParam;
import com.haven.simplej.rpc.annotation.RpcService;

/**
 * 唯一序列号生成器
 * @author: havenzhang
 * @date: 2019/1/10 23:26
 * @version 1.0
 */
@Doc(value = "基础设施，序列号生成器",author = "havenzhang",date = "20190110")
@RpcService(timeout = 100)
public interface ISequenceService extends BaseRpcService{

	/**
	 * 注册序列号信息
	 * @param  namespace 命名空间
	 * @param  seqKey 业务key
	 * @param  step 步长
	 * @return 唯一id
	 */
	@RpcMethod
	int register(String namespace, String seqKey, int step);

	/**
	 * 获取下一个序列号(短号，如：10001)
	 * @param namespace 命名空间
	 * @param seqKey 业务key
	 * @param length 序列号长度
	 * @return 短号序列号
	 */
	@RpcMethod
	long getNextShortSeqNo(@RpcParam(required = true) String namespace, String seqKey, @RpcParam(required = true) int length);


	/**
	 * 获取下一个序列号(长号，组成：yyyyMMddHHmmssSSS+id+seqValue)
	 * @param namespace 命名空间
	 * @param seqKey 业务key
	 * @return 短号序列号
	 */
	@RpcMethod
	String getNextLongSeqNo(String namespace, String seqKey);

}
