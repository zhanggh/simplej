package com.haven.accounting;

import lombok.Data;

/**
 * @author: havenzhang
 * @date: 2019/6/29 23:08
 * @version 1.0
 */
@Data
public class AccountResponse {
	private boolean success;
	private String respCode;
	private String respMsg;
	private long frozenId;
}
