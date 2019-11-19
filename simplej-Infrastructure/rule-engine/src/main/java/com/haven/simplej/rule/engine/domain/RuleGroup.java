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
import com.haven.simplej.rule.engine.domain.definition.RuleGroupDefinition;
/**
 * 规则包信息
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "rule_group", catalog = "1", schema = "rule_engine", indexes = { // catalog 定义为分表数
    @Index(name = "idx_name_rule_type", columnList = "name", unique = false), //
})
public class RuleGroup extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 所属业务场景id
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "biz_id", nullable = false, precision = 20, comment = "所属业务场景id", dataType="bigint")
	private Long bizId;

	/**
     * 规则组名称
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "name", nullable = false, length = 128, comment = "规则组名称", dataType="varchar")
	private String name;

	/**
     * 规则组执行类型：single_match-命中退出,full_match-全量匹配
     * @definition varchar(30) DEFAULT '' 
     */
    @ColumnProperty(name = "type", nullable = false, length = 30, comment = "规则组执行类型：single_match-命中退出,full_match-全量匹配", dataType="varchar")
	private String type;

	/**
     * 状态: 1:有效，0：无效
     * @definition tinyint(3) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "status", nullable = false, precision = 3, comment = "状态: 1:有效，0：无效", dataType="tinyint")
	private Byte status;

	public RuleGroup (){
		super();
	}
	
	public RuleGroup (RuleGroup that){
        super(that);
		this.bizId = that.bizId;
		this.name = that.name;
		this.type = that.type;
		this.status = that.status;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return RuleGroupDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = RuleGroupDefinition.values();
        return defs;
    }
}