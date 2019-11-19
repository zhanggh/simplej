package com.haven.simplej.rpc.model;

import com.haven.simplej.rpc.annotation.Doc;
import com.haven.simplej.rpc.annotation.RpcParam;
import lombok.Data;

/**
 * @author: havenzhang
 * @date: 2019/4/26 21:34
 * @version 1.0
 */
@Data
public class BaseRpcMessage<T> {

	/**
	 * 请求包类型：1-业务请求，0-心跳包
	 */
	private byte msgFlag;

	/**
	 * 报文头
	 */
	private RpcHeader header;

	/**
	 * 报文体
	 */
	private T body;

	/**
	 * 报文体，字节数据的方式，比如文件上传下载
	 */
	private byte[] bytesArray;

	@RpcParam
	@Doc("是否为文件流传输")
	private boolean fileTransfer;

	/**
	 *  request/response
	 */
	private byte msgType;
}
