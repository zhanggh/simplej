package com.haven.simplej.rule.engine.domain;
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
import com.haven.simplej.rule.engine.domain.definition.ExpressionDefinition;
/**
 * 规则表达式，一个规则对应多个表达式
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "expression", catalog = "1", schema = "rule_engine", indexes = { // catalog 定义为分表数
    @Index(name = "idx_rule_id", columnList = "rule_id", unique = false), //
})
public class Expression extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 表达式类型：if/then/else/normal_command
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "expr_type", nullable = false, length = 10, comment = "表达式类型：if/then/else/normal_command", dataType="varchar")
	private String exprType;

	/**
     * 操作参数表达式
     * @definition varchar(1000) DEFAULT '' 
     */
    @ColumnProperty(name = "expr_params", nullable = false, length = 1000, comment = "操作参数表达式", dataType="varchar")
	private String exprParams;

	/**
     * 操作符
     * @definition varchar(50) DEFAULT '' 
     */
    @ColumnProperty(name = "operator", nullable = false, length = 50, comment = "操作符", dataType="varchar")
	private String operator;

	/**
     * 操作符id，关联操作符信息
     * @definition bigint(20) DEFAULT '0' 
     */
    @ColumnProperty(name = "operator_id", nullable = false, precision = 19, comment = "操作符id，关联操作符信息", dataType="bigint")
	private Long operatorId;

	/**
     * 序号
     * @definition int(11) DEFAULT '1' 
     */
    @ColumnProperty(name = "order_num", nullable = false, precision = 10, comment = "序号", dataType="int")
	private Integer orderNum;

	/**
     * 规则id
     * @definition bigint(20) DEFAULT '0' 
     */
    @ColumnProperty(name = "rule_id", nullable = false, precision = 19, comment = "规则id", dataType="bigint")
	private Long ruleId;

	public Expression (){
		super();
	}
	
	public Expression (Expression that){
        super(that);
		this.exprType = that.exprType;
		this.exprParams = that.exprParams;
		this.operator = that.operator;
		this.operatorId = that.operatorId;
		this.orderNum = that.orderNum;
		this.ruleId = that.ruleId;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return ExpressionDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = ExpressionDefinition.values();
        return defs;
    }
}