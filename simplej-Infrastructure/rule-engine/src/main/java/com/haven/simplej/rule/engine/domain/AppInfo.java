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
import com.haven.simplej.rule.engine.domain.definition.AppInfoDefinition;
/**
 * 接入方信息
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "app_info", catalog = "1", schema = "rule_engine", indexes = { // catalog 定义为分表数
    @Index(name = "idx_name_rule_type", columnList = "name", unique = false), //
})
public class AppInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * app编号，接入的时候需要分配，确保唯一
     * @definition varchar(32) DEFAULT '' 
     */
    @ColumnProperty(name = "app_code", nullable = false, length = 32, comment = "app编号，接入的时候需要分配，确保唯一", dataType="varchar")
	private String appCode;

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
     * 接入方ip地址，多个以英文逗号分割
     * @definition varchar(512) DEFAULT '' 
     */
    @ColumnProperty(name = "ips", nullable = false, length = 512, comment = "接入方ip地址，多个以英文逗号分割", dataType="varchar")
	private String ips;

	/**
     * 签名密码
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "access_key", nullable = false, length = 128, comment = "签名密码", dataType="varchar")
	private String accessKey;

	/**
     * 状态: 1:有效，0：无效
     * @definition tinyint(3) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "status", nullable = false, precision = 3, comment = "状态: 1:有效，0：无效", dataType="tinyint")
	private Byte status;

	public AppInfo (){
		super();
	}
	
	public AppInfo (AppInfo that){
        super(that);
		this.appCode = that.appCode;
		this.name = that.name;
		this.description = that.description;
		this.ips = that.ips;
		this.accessKey = that.accessKey;
		this.status = that.status;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return AppInfoDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = AppInfoDefinition.values();
        return defs;
    }
}