package com.haven.simplej.rpc.registry.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.rpc.registry.domain.definition.UrlInfoDefinition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
/**
 * web服务url信息
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "url_info", catalog = "1", schema = "rpc_register", indexes = { // catalog 定义为分表数
    @Index(name = "idx_namespace", columnList = "namespace", unique = false), //
    @Index(name = "idx_uri", columnList = "uri", unique = false), //
})
public class UrlServiceInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * web服务uri
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "uri", nullable = false, length = 128, comment = "web服务uri", dataType="varchar")
	private String uri;

	/**
     * http method，如post,多个用英文逗号分隔
     * @definition varchar(32) DEFAULT '' 
     */
    @ColumnProperty(name = "method", nullable = false, length = 32, comment = "http method，如post,多个用英文逗号分隔", dataType="varchar")
	private String method;

	/**
     * http header，如：Content-Type=application/json，多个用于英文逗号分隔
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "header", nullable = false, length = 128, comment = "http header，如：Content-Type=application/json，多个用于英文逗号分隔", dataType="varchar")
	private String header;

	/**
     * web服务的schema，http/https
     * @definition varchar(6) DEFAULT 'http' 
     */
    @ColumnProperty(name = "url_schema", nullable = false, length = 6, comment = "web服务的schema，http/https", dataType=
			"varchar")
	private String urlSchema;

	/**
     * 命名空间，可以是唯一的域名
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "namespace", nullable = false, length = 128, comment = "命名空间，可以是唯一的域名", dataType="varchar")
	private String namespace;
	/**
	 * 服务状态，1-正常，0-未启用，2-关闭（无效）
	 * @definition tinyint(3) unsigned DEFAULT '1'
	 */
	@ColumnProperty(name = "status", nullable = false, precision = 3, comment = "服务状态，1-正常，0-未启用，2-关闭（无效）", dataType="tinyint")
	private Byte status;
	/**
	 * 心跳时间
	 * @definition bigint(20) unsigned DEFAULT '0'
	 */
	@ColumnProperty(name = "heartbeat_time", nullable = false, precision = 20, comment = "心跳时间", dataType="bigint")
	private Long heartbeatTime;
	/**
	 * 服务版本号
	 */
	private String urlVersion;
	/**
	 * 超时时间
	 */
	private Long timeout;

	public UrlServiceInfo(){
		super();
	}
	
	public UrlServiceInfo(UrlServiceInfo that){
        super(that);
		this.uri = that.uri;
		this.method = that.method;
		this.header = that.header;
		this.urlSchema = that.urlSchema;
		this.namespace = that.namespace;
		this.status = that.status;
		this.timeout = that.timeout;
		this.urlVersion = that.urlVersion;
		this.heartbeatTime = that.heartbeatTime;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return UrlInfoDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = UrlInfoDefinition.values();
        return defs;
    }
}