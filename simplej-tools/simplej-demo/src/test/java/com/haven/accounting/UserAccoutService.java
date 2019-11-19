package com.haven.accounting;

/**
 * @author: havenzhang
 * @date: 2019/6/29 23:03
 * @version 1.0
 */
public interface UserAccoutService {

	AccountResponse frozen(String userNo,long amount);

	boolean release2deduct(long frozenId);

	boolean cancel(long frozenId);
}
