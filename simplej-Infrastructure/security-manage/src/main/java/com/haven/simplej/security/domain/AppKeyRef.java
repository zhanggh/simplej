package com.haven.simplej.security.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.security.domain.definition.AppKeyRefDefinition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
/**
 * 操作日志表
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "app_key_ref", catalog = "1", schema = "authen", indexes = { // catalog 定义为分表数
    @Index(name = "idx_app_id", columnList = "app_id", unique = false), //
    @Index(name = "idx_create_time", columnList = "create_time", unique = false), //
    @Index(name = "idx_key_id", columnList = "key_id", unique = false), //
})
public class AppKeyRef extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 接入方id
     * @definition varchar(32) DEFAULT '' 
     */
    @ColumnProperty(name = "app_id", nullable = false, length = 32, comment = "接入方id", dataType="varchar")
	private String appId;

	/**
     * 秘钥id
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "key_id", nullable = false, precision = 20, comment = "秘钥id", dataType="bigint")
	private Long keyId;

	/**
     * 业务描述
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "business", nullable = false, length = 128, comment = "业务描述", dataType="varchar")
	private String business;

	/**
     * 保留字段，一般不建议使用
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "reserved", nullable = false, length = 128, comment = "保留字段，一般不建议使用", dataType="varchar")
	private String reserved;

	public AppKeyRef (){
		super();
	}
	
	public AppKeyRef (AppKeyRef that){
        super(that);
		this.appId = that.appId;
		this.keyId = that.keyId;
		this.business = that.business;
		this.reserved = that.reserved;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return AppKeyRefDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = AppKeyRefDefinition.values();
        return defs;
    }
}