package com.haven.simplej.rpc.model;

import com.haven.simplej.rpc.annotation.RpcStruct;
import com.haven.simplej.rpc.enums.MsgType;
import lombok.Data;

/**
 * @author: havenzhang
 * @date: 2018/04/26 11:40
 * @version 1.0
 */
@Data
@RpcStruct
public class RpcRequest<T> extends BaseRpcMessage {

	public RpcRequest(){
		super();
		setMsgType(MsgType.REQUEST.value);
	}
	public RpcRequest(BaseRpcMessage msg){
		super();
		setMsgType(MsgType.REQUEST.value);
		this.setHeader(msg.getHeader());
		this.setBody(msg.getBody());
		this.setMsgFlag(msg.getMsgFlag());
		this.setBytesArray(msg.getBytesArray());
		this.setFileTransfer(msg.isFileTransfer());
	}
}