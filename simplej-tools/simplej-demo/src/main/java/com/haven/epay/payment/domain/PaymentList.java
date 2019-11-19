package com.haven.epay.payment.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import lombok.*;
import com.haven.simplej.db.annotation.*;
import java.sql.*;
import javax.persistence.*;
import java.math.BigDecimal;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import org.springframework.format.annotation.DateTimeFormat;
import org.apache.commons.lang3.StringUtils;
import com.haven.epay.payment.domain.definition.PaymentListDefinition;
/**
 * 交易流水表
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "payment_list", catalog = "1", schema = "test", indexes = { // catalog 定义为分表数
    @Index(name = "idx_transaction_id", columnList = "transaction_id", unique = false), //
})
public class PaymentList extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 交易流水号
     * @definition varchar(64) DEFAULT NULL 
     */
    @ColumnProperty(name = "transaction_id", nullable = false, length = 64, comment = "交易流水号", dataType="varchar")
	private String transactionId;

	/**
     * 交易金额，单位：分
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "amt", nullable = false, precision = 20, comment = "交易金额，单位：分", dataType="bigint")
	private Long amt;

	/**
     * 交易时间
     * @definition timestamp DEFAULT '1980-01-01 00:00:00' 
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ColumnProperty(name = "tx_date", nullable = false, comment = "交易时间", dataType="timestamp")
	private Timestamp txDate;

	/**
     * 卡token
     * @definition varchar(64) DEFAULT '' 
     */
    @ColumnProperty(name = "card_token", nullable = false, length = 64, comment = "卡token", dataType="varchar")
	private String cardToken;

	/**
     * 保留字段
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "reserved", nullable = false, length = 128, comment = "保留字段", dataType="varchar")
	private String reserved;

	public PaymentList (){
		super();
	}
	
	public PaymentList (PaymentList that){
        super(that);
		this.transactionId = that.transactionId;
		this.amt = that.amt;
		this.txDate = that.txDate;
		this.cardToken = that.cardToken;
		this.reserved = that.reserved;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return PaymentListDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = PaymentListDefinition.values();
        return defs;
    }
}