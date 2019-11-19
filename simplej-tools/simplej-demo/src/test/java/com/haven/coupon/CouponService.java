package com.haven.coupon;

/**
 * @author: havenzhang
 * @date: 2019/6/29 22:54
 * @version 1.0
 */
public interface CouponService {

	CouponFrozenResponse frozen(Long couponId);

	boolean release2deduct(long frozenId);

	boolean cancel(long frozenId);
}
