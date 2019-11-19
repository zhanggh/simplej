package com.haven.simplej.rpc.config.model;

import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcParam;
import com.haven.simplej.rpc.annotation.RpcStruct;
import lombok.Data;

import java.util.List;

/**
 * 配置属性响应模型
 * @author: havenzhang
 * @date: 2019/6/11 23:24
 * @version 1.0
 */
@Data
@RpcStruct
public class ConfigItemResponse {


	@Doc("是否有修改过")
	@RpcParam
	private boolean change;
	/**
	 * 上个版本的旧属性（全量）
	 */
	@Doc("上个版本的旧属性（全量）")
	private List<ConfigItemRpcModel> oldProps;
	/**
	 * 当前最新版本的旧属性（全量）
	 */
	@Doc("当前最新版本的旧属性（全量）")
	private List<ConfigItemRpcModel> newProps;
}
