package com.haven.simplej.exception;

/**
 * CheckedException的wrapper.
 *
 * 返回Message时, 将返回内层Exception的Message.
 */
public class UncheckedException extends RuntimeException {

	private static final long serialVersionUID = 4140223302171577501L;

	public UncheckedException(Throwable wrapped) {
		super(wrapped);
	}

	public UncheckedException(String message) {
		super(new Exception(message));
	}

	@Override
	public String getMessage() {

		return super.getCause().getMessage();
	}
}