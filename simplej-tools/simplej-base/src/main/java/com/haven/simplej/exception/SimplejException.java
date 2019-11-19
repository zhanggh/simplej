package com.haven.simplej.exception;

import lombok.Data;

/**
 * 自定义的web响应异常，对于web的请求响应来说，只关心两个结果，成功、失败
 * @author haven.zhang
 * @date 2019/1/16.
 */
@Data
public class SimplejException extends RuntimeException {
	private static final long serialVersionUID = 4140223302171577501L;

	private String code;

	private String errorMsg;

	public SimplejException(Throwable wrapped) {
		super(wrapped);
	}

	public SimplejException(String message) {
		super(new Exception(message));
	}

	public SimplejException(String code,String errorMsg,Throwable wrapped) {
		super(wrapped);
		this.code = code;
		this.errorMsg = errorMsg;
	}


	@Override
	public String getMessage() {

		return super.getCause().getMessage();
	}
}
