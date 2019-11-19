package com.haven.simplej.rpc.exception;

import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.rpc.model.RpcHeader;
import lombok.Data;

/**
 * @author: havenzhang
 * @date: 2019/5/7 22:00
 * @version 1.0
 */
@Data
public class RpcException extends UncheckedException {

	private String respCode;
	private String respMsg;

	private RpcHeader header;


	public RpcException(String errorCode,String errorMsg,RpcHeader header) {
		super(new Exception(errorMsg));
		this.respCode = errorCode;
		this.respMsg = errorMsg;
		this.header = header;
	}
	public RpcException(RpcError error,RpcHeader header) {
		super(new Exception(error.getErrorMsg()));
		this.respCode = error.getErrorCode();
		this.respMsg = error.getErrorMsg();
		this.header = header;
	}
	public RpcException(RpcError error) {
		super(new Exception(error.getErrorMsg()));
		this.respCode = error.getErrorCode();
		this.respMsg = error.getErrorMsg();
	}

	public RpcException(RpcError error,Throwable wrapped) {
		super(wrapped);
		this.respCode = error.getErrorCode();
		this.respMsg = error.getErrorMsg();
	}

	public RpcException(RpcError error,String errorMsg) {
		super(new Exception(errorMsg));
		this.respCode = error.getErrorCode();
		this.respMsg = errorMsg;
	}
}
