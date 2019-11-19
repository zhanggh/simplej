package com.haven.epay.payment.domain.definition;
import com.haven.simplej.db.annotation.ColumnProperty;
import lombok.*;
import com.haven.simplej.db.annotation.*;
import java.sql.*;
import javax.persistence.*;
import java.math.BigDecimal;
import com.haven.simplej.db.base.BaseDomain;
import org.springframework.format.annotation.DateTimeFormat;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.util.DomainUtil;
import com.haven.epay.payment.domain.PaymentList;


/**
 * 交易流水表 字段属性枚举类
 */
public enum PaymentListDefinition implements EnumDomainDef {

	id,
	transactionId,
	amt,
	txDate,
	cardToken,
	createTime,
	updateTime,
	createdBy,
	updatedBy,
	reserved;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(PaymentList.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "payment_list";
	}
}