package com.haven.simplej.security.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.security.domain.definition.CertificateInfoDefinition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
/**
 * 接入方对应的key关联管理表
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "certificate_info", catalog = "1", schema = "authen", indexes = { // catalog 定义为分表数
    @Index(name = "idx_cert_sequence", columnList = "cert_sequence", unique = false), //
    @Index(name = "idx_create_time", columnList = "create_time", unique = false), //
    @Index(name = "idx_key_id", columnList = "key_id", unique = false), //
})
public class CertificateInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 证书序列号
     * @definition varchar(64) DEFAULT '' 
     */
    @ColumnProperty(name = "cert_sequence", nullable = false, length = 64, comment = "证书序列号", dataType="varchar")
	private String certSequence;

	/**
     * 证书格式：der/cer/crt/pem/jks/pfx/p12
     * @definition varchar(32) DEFAULT ''der'' 
     */
    @ColumnProperty(name = "format", nullable = true, length = 32, comment = "证书格式：der/cer/crt/pem/jks/pfx/p12", dataType="varchar")
	private String format;

	/**
     * 秘钥id，所有证书保存的时候，会把秘钥存储在key_info表
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "key_id", nullable = false, precision = 20, comment = "秘钥id，所有证书保存的时候，会把秘钥存储在key_info表", dataType="bigint")
	private Long keyId;

	/**
     * 证书类型：1-公钥证书，2-私钥证书
     * @definition varchar(20) DEFAULT '' 
     */
    @ColumnProperty(name = "cert_type", nullable = false, length = 20, comment = "证书类型：1-公钥证书，2-私钥证书", dataType="varchar")
	private String certType;

	/**
     * 证书文件内容
     * @definition text DEFAULT NULL 
     */
    @ColumnProperty(name = "cert_context", nullable = true, length = 65535, comment = "证书文件内容", dataType="text")
	private String certContext;

	/**
     * 保留字段，一般不建议使用
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "reserved", nullable = false, length = 128, comment = "保留字段，一般不建议使用", dataType="varchar")
	private String reserved;

	public CertificateInfo (){
		super();
	}
	
	public CertificateInfo (CertificateInfo that){
        super(that);
		this.certSequence = that.certSequence;
		this.format = that.format;
		this.keyId = that.keyId;
		this.certType = that.certType;
		this.certContext = that.certContext;
		this.reserved = that.reserved;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return CertificateInfoDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = CertificateInfoDefinition.values();
        return defs;
    }
}