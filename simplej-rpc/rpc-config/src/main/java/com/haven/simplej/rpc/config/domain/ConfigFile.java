package com.haven.simplej.rpc.config.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.rpc.config.domain.definition.ConfigFileDefinition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.sql.Timestamp;
/**
 * 属性文件信息
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "config_file", schema = "rpc_config", indexes = { // catalog 定义为分表数
    @Index(name = "idx_namespace_file_name", columnList = "namespace, file_name", unique = true), //
})
public class ConfigFile extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 所属的命名空间
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "namespace", nullable = false, length = 128, comment = "所属的命名空间", dataType="varchar")
	private String namespace;

	/**
     * 文件名
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "file_name", nullable = false, length = 128, comment = "文件名", dataType="varchar")
	private String fileName;

	/**
     * 文件内容
     * @definition varchar(1024) DEFAULT '' 
     */
    @ColumnProperty(name = "file_context", nullable = false, length = 1024, comment = "文件内容", dataType="varchar")
	private String fileContext;

	/**
     * xml/propertie/yml
     * @definition varchar(1) DEFAULT '1' 
     */
    @ColumnProperty(name = "file_type", nullable = false, length = 1, comment = "xml/propertie/yml", dataType="varchar")
	private String fileType;

	/**
     * 属性版本号
     * @definition int(11) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "file_version", nullable = false, precision = 10, comment = "属性版本号", dataType="int")
	private Integer fileVersion;

	/**
     * 属性描述
     * @definition varchar(255) DEFAULT '' 
     */
    @ColumnProperty(name = "description", nullable = false, length = 255, comment = "属性描述", dataType="varchar")
	private String description;

	/**
     * init-初始状态，未生效, valid-有效状态,invalid-无效状态
     * @definition varchar(11) DEFAULT 'init' 
     */
    @ColumnProperty(name = "status", nullable = false, length = 11, comment = "init-初始状态，未生效, valid-有效状态,invalid-无效状态", dataType="varchar")
	private String status;

	/**
     * 生效时间
     * @definition timestamp(3) DEFAULT '1970-01-02 00:00:00.000' 
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ColumnProperty(name = "effective_time", nullable = false, comment = "生效时间", dataType="timestamp")
	private Timestamp effectiveTime;

	/**
     * 失效时间
     * @definition timestamp(3) DEFAULT '2038-01-02 00:00:00.000' 
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ColumnProperty(name = "expire_time", nullable = false, comment = "失效时间", dataType="timestamp")
	private Timestamp expireTime;

	public ConfigFile (){
		super();
	}
	
	public ConfigFile (ConfigFile that){
        super(that);
		this.namespace = that.namespace;
		this.fileName = that.fileName;
		this.fileContext = that.fileContext;
		this.fileType = that.fileType;
		this.fileVersion = that.fileVersion;
		this.description = that.description;
		this.status = that.status;
		this.effectiveTime = that.effectiveTime;
		this.expireTime = that.expireTime;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return ConfigFileDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = ConfigFileDefinition.values();
        return defs;
    }
}