package com.haven.transaction.manager;

import com.haven.accounting.UserAccoutService;
import com.haven.coupon.CouponService;
import lombok.Builder;

/**
 * @author: havenzhang
 * @date: 2019/6/30 15:08
 * @version 1.0
 */

public class TransactionManager {

	public static <T> T getResource(Class<T> resourceClass) {
		return null;
	}

	public static long start(long transactionId) {
		return 0;
	}

	public static void end(long transactionId) {
	}

	public static void rollback() {
	}

	public static int getStatus() {
		return 0;
	}
}
