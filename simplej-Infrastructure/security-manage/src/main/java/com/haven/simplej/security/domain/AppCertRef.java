package com.haven.simplej.security.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.security.domain.definition.AppCertRefDefinition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
/**
 * 接入方对应的证书关联管理表
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "app_cert_ref", catalog = "1", schema = "authen", indexes = { // catalog 定义为分表数
    @Index(name = "idx_app_id", columnList = "app_id", unique = false), //
    @Index(name = "idx_cert_id", columnList = "cert_id", unique = false), //
    @Index(name = "idx_create_time", columnList = "create_time", unique = false), //
})
public class AppCertRef extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 接入方id
     * @definition varchar(32) DEFAULT '' 
     */
    @ColumnProperty(name = "app_id", nullable = false, length = 32, comment = "接入方id", dataType="varchar")
	private String appId;

	/**
     * 证书记录id
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "cert_id", nullable = false, precision = 20, comment = "证书记录id", dataType="bigint")
	private Long certId;

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

	public AppCertRef (){
		super();
	}
	
	public AppCertRef (AppCertRef that){
        super(that);
		this.appId = that.appId;
		this.certId = that.certId;
		this.business = that.business;
		this.reserved = that.reserved;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return AppCertRefDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = AppCertRefDefinition.values();
        return defs;
    }
}