package com.haven.simplej.security.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.security.domain.definition.KeyInfoDefinition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
/**
 * 秘钥信息表
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "key_info", catalog = "1", schema = "authen", indexes = { // catalog 定义为分表数
    @Index(name = "idx_created_by", columnList = "created_by", unique = false), //
    @Index(name = "idx_create_time", columnList = "create_time", unique = false), //
})
public class KeyInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 秘钥类型：aes/des/desede/r4/r2/rsa/dsa/ec/other
     * @definition varchar(16) DEFAULT '' 
     */
    @ColumnProperty(name = "key_type", nullable = false, length = 16, comment = "秘钥类型：aes/des/desede/r4/r2/rsa/dsa/ec/other", dataType="varchar")
	private String keyType;

	/**
     * 偏移量
     * @definition varchar(16) DEFAULT '' 
     */
    @ColumnProperty(name = "iv", nullable = false, length = 16, comment = "偏移量", dataType="varchar")
	private String iv;

	/**
     * 模式，如：AES/ECB/PKCS5Padding
     * @definition varchar(32) DEFAULT '' 
     */
    @ColumnProperty(name = "mode", nullable = false, length = 32, comment = "模式，如：AES/ECB/PKCS5Padding", dataType="varchar")
	private String mode;

	/**
     * 秘钥加密版本号
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "version", nullable = false, length = 10, comment = "秘钥加密版本号", dataType="varchar")
	private String version;

	/**
     * 秘钥名称
     * @definition varchar(32) DEFAULT '' 
     */
    @ColumnProperty(name = "key_name", nullable = false, length = 32, comment = "秘钥名称", dataType="varchar")
	private String keyName;

	/**
     * 秘钥内容，经过了主密钥加密并且base64后的字符串保存于此
     * @definition varchar(512) DEFAULT '' 
     */
    @ColumnProperty(name = "key", nullable = false, length = 512, comment = "秘钥内容，经过了主密钥加密并且base64后的字符串保存于此", dataType="varchar")
	private String key;

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

	public KeyInfo (){
		super();
	}
	
	public KeyInfo (KeyInfo that){
        super(that);
		this.keyType = that.keyType;
		this.iv = that.iv;
		this.mode = that.mode;
		this.version = that.version;
		this.keyName = that.keyName;
		this.key = that.key;
		this.business = that.business;
		this.reserved = that.reserved;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return KeyInfoDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = KeyInfoDefinition.values();
        return defs;
    }
}