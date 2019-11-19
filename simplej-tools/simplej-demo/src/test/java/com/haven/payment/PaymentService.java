package com.haven.payment;

/**
 * @author: havenzhang
 * @date: 2019/6/29 22:52
 * @version 1.0
 */
public interface PaymentService {

	public PaymentResponse payment(String userNo,long amout,Long couponId);
}
