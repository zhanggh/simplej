package com.haven.simplej.rule.engine.exception;

import com.haven.simplej.exception.UncheckedException;
import com.haven.simplej.rule.engine.emuns.RuleError;
import com.vip.vjtools.vjkit.base.ExceptionUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: havenzhang
 * @date: 2019/4/18 22:12
 * @version 1.0
 */
@Data
public class RuleException extends UncheckedException {

	/**
	 * 错误码
	 */
	private String errorCode;

	/**
	 * 错误信息
	 */
	private String errorMsg;

	public RuleException(Throwable wrapped) {
		super(wrapped);
		this.errorCode = RuleError.generate_error.name();
		this.errorMsg = StringUtils.isEmpty(wrapped.getMessage()) ?
				ExceptionUtil.toStringWithRootCause(wrapped) :
				wrapped.getMessage();
	}

	public RuleException(Throwable wrapped, RuleError errorCode) {
		super(wrapped);
		this.errorCode = errorCode.name();
		this.errorMsg = StringUtils.isEmpty(wrapped.getMessage()) ?
				ExceptionUtil.toStringWithRootCause(wrapped) :
				wrapped.getMessage();
	}

	public RuleException(Throwable wrapped, RuleError errorCode, String errorMsg) {
		super(wrapped);
		this.errorCode = errorCode.name();
		this.errorMsg = errorMsg;
	}
}
