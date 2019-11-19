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
import com.haven.simplej.rule.engine.domain.definition.RuleItemDefinition;
/**
 * 规则配置信息
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "rule_item", catalog = "1", schema = "rule_engine", indexes = { // catalog 定义为分表数
    @Index(name = "idx_name_rule_type", columnList = "name, rule_type", unique = false), //
})
public class RuleItem extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 规则类型
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "rule_type", nullable = false, length = 10, comment = "规则类型", dataType="varchar")
	private String ruleType;

	/**
     * 规则名称
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "name", nullable = false, length = 128, comment = "规则名称", dataType="varchar")
	private String name;

	/**
     * 英文名称
     * @definition varchar(32) DEFAULT '' 
     */
    @ColumnProperty(name = "en_name", nullable = false, length = 32, comment = "英文名称", dataType="varchar")
	private String enName;

	/**
     * 规则参数，json格式
     * @definition varchar(1024) DEFAULT '' 
     */
    @ColumnProperty(name = "params", nullable = false, length = 1024, comment = "规则参数，json格式", dataType="varchar")
	private String params;

	/**
     * 脚本代码
     * @definition text DEFAULT NULL 
     */
    @ColumnProperty(name = "script_code", nullable = true, length = 65535, comment = "脚本代码", dataType="text")
	private String scriptCode;

	/**
     * 返回类型
     * @definition varchar(256) DEFAULT '' 
     */
    @ColumnProperty(name = "return_type", nullable = false, length = 256, comment = "返回类型", dataType="varchar")
	private String returnType;

	public RuleItem (){
		super();
	}
	
	public RuleItem (RuleItem that){
        super(that);
		this.ruleType = that.ruleType;
		this.name = that.name;
		this.enName = that.enName;
		this.params = that.params;
		this.scriptCode = that.scriptCode;
		this.returnType = that.returnType;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return RuleItemDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = RuleItemDefinition.values();
        return defs;
    }
}