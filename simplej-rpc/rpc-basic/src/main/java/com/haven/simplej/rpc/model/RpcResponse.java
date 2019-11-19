package com.haven.simplej.rpc.model;

import com.haven.simplej.rpc.annotation.RpcStruct;
import com.haven.simplej.rpc.enums.MsgType;
import lombok.Data;
import lombok.ToString;

/**
 * @author: havenzhang
 * @date: 2018/04/26 11:40
 * @version 1.0
 */
@Data
@ToString
@RpcStruct
public class RpcResponse<T> extends BaseRpcMessage {


	public RpcResponse(){
		super();
		setMsgType(MsgType.RESPONSE.value);
	}

	public RpcResponse(BaseRpcMessage msg){
		super();
		setMsgType(MsgType.RESPONSE.value);
		this.setHeader(msg.getHeader());
		this.setBody(msg.getBody());
		this.setMsgFlag(msg.getMsgFlag());
		this.setBytesArray(msg.getBytesArray());
		this.setFileTransfer(msg.isFileTransfer());
	}
}