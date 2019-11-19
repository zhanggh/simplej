package com.haven.simplej.rpc.registry.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import lombok.*;

import javax.persistence.*;

import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import org.apache.commons.lang3.StringUtils;
import com.haven.simplej.rpc.registry.domain.definition.ServiceInfoDefinition;
/**
 * rpc服务信息
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "service_info", schema = "rpc_register", indexes = { // catalog 定义为分表数
    @Index(name = "idx_service_name_version_namespace_status", columnList = "service_name, namespace, service_version, status", unique = true), //
})
public class ServiceInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 命名空间，可以是模块唯一标志，比如域名
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "namespace", nullable = false, length = 128, comment = "命名空间，可以是模块唯一标志，比如域名", dataType="varchar")
	private String namespace;

	/**
     * 服务名
     * @definition varchar(256) DEFAULT '' 
     */
    @ColumnProperty(name = "service_name", nullable = false, length = 256, comment = "服务名", dataType="varchar")
	private String serviceName;

	/**
     * 乐观锁版本号
     * @definition int(11) DEFAULT '0' 
     */
    @ColumnProperty(name = "version", nullable = false, precision = 10, comment = "乐观锁版本号", dataType="int")
	private Integer version;

	/**
     * 服务状态，1-正常，0-未启用，2-关闭（无效）
     * @definition tinyint(3) unsigned DEFAULT '1' 
     */
    @ColumnProperty(name = "status", nullable = false, precision = 3, comment = "服务状态，1-正常，0-未启用，2-关闭（无效）", dataType="tinyint")
	private Byte status;

	/**
     * 服务版本号
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "service_version", nullable = false, length = 10, comment = "服务版本号", dataType="varchar")
	private String serviceVersion;

	/**
     * 方法信息
     * @definition varchar(2048) DEFAULT '' 
     */
    @ColumnProperty(name = "method_info", nullable = false, length = 2048, comment = "方法信息", dataType="varchar")
	private String methodInfo;

	/**
     * 服务类型：BUSINESS_SERVER-普通业务服务，PROXY-代理服务，RPC_CENTER-rpc主控服务，RPC_REGISTER 注册中心
     * @definition varchar(150) DEFAULT '' 
     */
    @ColumnProperty(name = "server_type", nullable = false, length = 150, comment = "服务类型：BUSINESS_SERVER-普通业务服务，PROXY-代理服务，RPC_CENTER-rpc主控服务，RPC_REGISTER 注册中心", dataType="varchar")
	private String serverType;

	/**
     * 备注
     * @definition varchar(150) DEFAULT ''
     */
    @ColumnProperty(name = "remark", nullable = false, length = 150, comment = "备注", dataType="varchar")
	private String remark;

	/**
     * 超时时间
     * @definition bigint(20) unsigned DEFAULT '60000' 
     */
    @ColumnProperty(name = "timeout", nullable = false, precision = 20, comment = "超时时间", dataType="bigint")
	private Long timeout;

	/**
     * 心跳时间戳
     * @definition bigint(20) DEFAULT '0' 
     */
    @ColumnProperty(name = "heartbeat_time", nullable = false, precision = 19, comment = "心跳时间戳", dataType="bigint")
	private Long heartbeatTime;

	public ServiceInfo (){
		super();
	}
	
	public ServiceInfo (ServiceInfo that){
        super(that);
		this.namespace = that.namespace;
		this.serviceName = that.serviceName;
		this.version = that.version;
		this.status = that.status;
		this.serviceVersion = that.serviceVersion;
		this.methodInfo = that.methodInfo;
		this.remark = that.remark;
		this.serverType = that.serverType;
		this.timeout = that.timeout;
		this.heartbeatTime = that.heartbeatTime;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return ServiceInfoDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = ServiceInfoDefinition.values();
        return defs;
    }
}