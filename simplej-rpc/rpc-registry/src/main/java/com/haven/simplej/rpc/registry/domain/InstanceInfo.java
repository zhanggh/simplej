package com.haven.simplej.rpc.registry.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import org.apache.commons.lang3.StringUtils;
import com.haven.simplej.rpc.registry.domain.definition.InstanceInfoDefinition;
/**
 * rpc服务的实例信息
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "instance_info", schema = "rpc_register", indexes = { // catalog 定义为分表数
    @Index(name = "idx_host_port_idc", columnList = "host, port, idc, namespace", unique = true), //
    @Index(name = "idx_namespace", columnList = "namespace", unique = false), //
})
public class InstanceInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 实例的ip
     * @definition varchar(15) DEFAULT '' 
     */
    @ColumnProperty(name = "host", nullable = false, length = 15, comment = "实例的ip", dataType="varchar")
	private String host;

	/**
     * 服务实例端口
     * @definition int(11) DEFAULT '0' 
     */
    @ColumnProperty(name = "port", nullable = false, precision = 10, comment = "服务实例端口", dataType="int")
	private Integer port;

	/**
     * 服务状态，1-正常，0-未启用，2-关闭（无效）
     * @definition tinyint(3) unsigned DEFAULT '1' 
     */
    @ColumnProperty(name = "status", nullable = false, precision = 3, comment = "服务状态，1-正常，0-未启用，2-关闭（无效）", dataType="tinyint")
	private Byte status;

	/**
     * idc标志
     * @definition varchar(64) DEFAULT '' 
     */
    @ColumnProperty(name = "idc", nullable = false, length = 64, comment = "idc标志", dataType="varchar")
	private String idc;

	/**
     * 命名空间（模块编号），每一个实例对应一个namespace
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "namespace", nullable = false, length = 128, comment = "命名空间（模块编号），每一个实例对应一个namespace", dataType="varchar")
	private String namespace;

	/**
     * 备注
     * @definition varchar(150) DEFAULT '' 
     */
    @ColumnProperty(name = "remark", nullable = false, length = 150, comment = "备注", dataType="varchar")
	private String remark;

	/**
     * 心跳时间戳
     * @definition bigint(20) DEFAULT '0' 
     */
    @ColumnProperty(name = "heartbeat_time", nullable = false, precision = 19, comment = "心跳时间戳", dataType="bigint")
	private Long heartbeatTime;

	/**
     * 流量权重
     * @definition decimal(10,0) DEFAULT '0' 
     */
    @ColumnProperty(name = "weight", nullable = false, precision = 10, comment = "流量权重", dataType="decimal")
	private BigDecimal weight;

	/**
     * 集合id
     * @definition varchar(32) DEFAULT '' 
     */
    @ColumnProperty(name = "region_id", nullable = false, length = 32, comment = "集合id", dataType="varchar")
	private String regionId;

	/**
     * proxy服务的tcp端口
     * @definition int(11) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "proxy_port", nullable = false, precision = 10, comment = "proxy服务的tcp端口", dataType="int")
	private Integer proxyPort;

	/**
     * 服务的http监听端口
     * @definition int(11) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "http_port", nullable = false, precision = 10, comment = "服务的http监听端口", dataType="int")
	private Integer httpPort;

	/**
     * proxy服务的http监听端口
     * @definition int(11) unsigned DEFAULT '0'
     */
    @ColumnProperty(name = "proxy_http_port", nullable = false, precision = 10, comment = "proxy服务的http监听端口", dataType="int")
	private Integer proxyHttpPort;

	public InstanceInfo (){
		super();
	}
	
	public InstanceInfo (InstanceInfo that){
        super(that);
		this.host = that.host;
		this.port = that.port;
		this.status = that.status;
		this.idc = that.idc;
		this.namespace = that.namespace;
		this.remark = that.remark;
		this.heartbeatTime = that.heartbeatTime;
		this.weight = that.weight;
		this.regionId = that.regionId;
		this.proxyPort = that.proxyPort;
		this.proxyHttpPort = that.proxyHttpPort;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return InstanceInfoDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = InstanceInfoDefinition.values();
        return defs;
    }
}