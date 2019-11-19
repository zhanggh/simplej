package com.haven.simplej.rpc.auth.model;

import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcStruct;
import lombok.Data;

import java.util.List;

/**
 * @author: havenzhang
 * @date: 2018/10/13 19:05
 * @version 1.0
 */
@RpcStruct
@Doc("目标服务信息")
@Data
public class RpcServerModel {

	/**
	 * 目标服务的命名空间
	 */
	private String serverNamespace;
	/**
	 * 目标服务方法列表
	 */
	private List<String> methodIds;

}
