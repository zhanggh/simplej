package com.haven.coupon;

import lombok.Data;

/**
 * @author: havenzhang
 * @date: 2019/6/29 22:59
 * @version 1.0
 */
@Data
public class CouponFrozenResponse {
	private boolean success;
	private String respCode;
	private String respMsg;
	private long frozenId;
	private long frozenAmount;
}
