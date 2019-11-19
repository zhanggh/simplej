package com.haven.payment;

import com.haven.accounting.AccountResponse;
import com.haven.accounting.UserAccoutService;
import com.haven.coupon.CouponFrozenResponse;
import com.haven.coupon.CouponService;
import com.haven.simplej.exception.SimplejException;
import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.transaction.manager.TransactionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: havenzhang
 * @date: 2019/6/29 22:51
 * @version 1.0
 */
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	/**
	 * 优惠券服务
	 */
	CouponService couponService;

	/**
	 * 账户服务
	 */
	UserAccoutService accoutService;


	public PaymentServiceImpl() {
		couponService = TransactionManager.getResource(CouponService.class);
		accoutService = TransactionManager.getResource(UserAccoutService.class);
	}

	@Override
	public PaymentResponse payment(String userNo, long amount, Long couponId) {
		PaymentResponse response = new PaymentResponse();
		//开启事务
		long transactionId = System.currentTimeMillis();
		TransactionManager.start(transactionId);
		try {
			//冻结优惠券
			CouponFrozenResponse couponResponse = couponService.frozen(couponId);
			//冻结用户账户余额
			AccountResponse accountResponse = accoutService.frozen(userNo, amount - couponResponse.getFrozenAmount());
			//解冻并核销优惠券
			couponService.release2deduct(couponResponse.getFrozenId());
			//解冻并扣除额度
			accoutService.release2deduct(accountResponse.getFrozenId());
			response.setRespCode("0");
		} catch (SimplejException e) {
			log.error("payment transaction error", e);
			//检查事务状态，可以自定义返回结果
			checkStatus(response);
		} catch (Exception e) {
			log.error("payment transaction error", e);
			checkStatus(response);
		} finally {
			//结束事务
			TransactionManager.end(transactionId);
		}
		return response;
	}

	private void checkStatus(PaymentResponse response) {
		if (TransactionManager.getStatus() == 1) {
			// custom logic
			response.setRespCode("99");
			response.setRespMsg("rollback");
		}
	}
}
