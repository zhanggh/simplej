package com.haven.epay.payment.service.impl;

import com.haven.simplej.db.annotation.DataSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import com.haven.simplej.db.annotation.DBShardingStrategy;
import lombok.extern.slf4j.Slf4j;
import com.haven.epay.payment.model.PaymentListModel;
import com.haven.epay.payment.service.PaymentListService;
import com.haven.simplej.db.base.BaseServiceImpl2;

/**
 * 交易流水表 Service implements
 */
@Slf4j
@Service
@Order(100000)
@DataSource(dbName = "test")
public class PaymentListServiceImpl extends BaseServiceImpl2<PaymentListModel> implements PaymentListService {


	@Override
	public String query(String orderId) {
		return null;
	}

	@Override
	public String get(String orderId, int param2) {
		log.info("PaymentListServiceImpl orderId:{} param2:{}", orderId, param2);
		return "heloo";
	}
}