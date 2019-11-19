package com.haven.simplej.rpc.config.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.rpc.config.domain.definition.ConfigItemHistoryDefinition;
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
 * 属性配置信息日志
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "config_item_history", schema = "rpc_config", indexes = { // catalog 定义为分表数
    @Index(name = "idx_namespace_item_name", columnList = "namespace, item_name", unique = true), //
})
public class ConfigItemHistory extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 原记录主键
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "org_id", nullable = false, precision = 20, comment = "原记录主键", dataType="bigint")
	private Long orgId;

	/**
     * 所属的命名空间
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "namespace", nullable = false, length = 128, comment = "所属的命名空间", dataType="varchar")
	private String namespace;

	/**
     * 属性名
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "item_name", nullable = false, length = 128, comment = "属性名", dataType="varchar")
	private String itemName;

	/**
     * 属性值
     * @definition varchar(1024) DEFAULT '' 
     */
    @ColumnProperty(name = "item_value", nullable = false, length = 1024, comment = "属性值", dataType="varchar")
	private String itemValue;

	/**
     * 1-普通属性项，2-配置文件
     * @definition varchar(1) DEFAULT '1' 
     */
    @ColumnProperty(name = "item_type", nullable = false, length = 1, comment = "1-普通属性项，2-配置文件", dataType="varchar")
	private String itemType;

	/**
     * 属性版本号
     * @definition int(11) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "item_version", nullable = false, precision = 10, comment = "属性版本号", dataType="int")
	private Integer itemVersion;

	/**
     * 属性描述
     * @definition varchar(255) DEFAULT '' 
     */
    @ColumnProperty(name = "description", nullable = false, length = 255, comment = "属性描述", dataType="varchar")
	private String description;

	/**
     * init-初始状态，未生效, valid-有效状态,invalid-无效状态
     * @definition varchar(16) DEFAULT 'init' 
     */
    @ColumnProperty(name = "status", nullable = false, length = 16, comment = "init-初始状态，未生效, valid-有效状态,invalid-无效状态", dataType="varchar")
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

	public ConfigItemHistory (){
		super();
	}
	
	public ConfigItemHistory (ConfigItemHistory that){
        super(that);
		this.orgId = that.orgId;
		this.namespace = that.namespace;
		this.itemName = that.itemName;
		this.itemValue = that.itemValue;
		this.itemType = that.itemType;
		this.itemVersion = that.itemVersion;
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
        return ConfigItemHistoryDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = ConfigItemHistoryDefinition.values();
        return defs;
    }
}