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
import com.haven.simplej.rule.engine.domain.definition.BusinessDefinition;
/**
 * 接入方信息
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "business", catalog = "1", schema = "rule_engine", indexes = { // catalog 定义为分表数
    @Index(name = "idx_name", columnList = "name", unique = false), //
})
public class Business extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 业务所属接入方
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "app_id", nullable = false, precision = 20, comment = "业务所属接入方", dataType="bigint")
	private Long appId;

	/**
     * 接入方名称
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "name", nullable = false, length = 128, comment = "接入方名称", dataType="varchar")
	private String name;

	/**
     * 接入方描述
     * @definition varchar(512) DEFAULT '' 
     */
    @ColumnProperty(name = "description", nullable = false, length = 512, comment = "接入方描述", dataType="varchar")
	private String description;

	/**
     * 状态: 1:有效，0：无效
     * @definition tinyint(3) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "status", nullable = false, precision = 3, comment = "状态: 1:有效，0：无效", dataType="tinyint")
	private Byte status;

	public Business (){
		super();
	}
	
	public Business (Business that){
        super(that);
		this.appId = that.appId;
		this.name = that.name;
		this.description = that.description;
		this.status = that.status;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return BusinessDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = BusinessDefinition.values();
        return defs;
    }
}