package com.haven.simplej.rpc.registry.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import lombok.*;

import javax.persistence.*;

import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import org.apache.commons.lang3.StringUtils;
import com.haven.simplej.rpc.registry.domain.definition.ServiceInstanceRefDefinition;
/**
 * 服务与实例的关联关系
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "service_instance_ref", catalog = "1", schema = "rpc_register", indexes = { // catalog 定义为分表数
    @Index(name = "idx_instance_id", columnList = "instance_id", unique = false), //
    @Index(name = "idx_service_id", columnList = "service_id", unique = false), //
})
public class ServiceInstanceRef extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 服务记录id
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "service_id", nullable = false, precision = 20, comment = "服务记录id", dataType="bigint")
	private Long serviceId;

	/**
     * 实例的记录id
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "instance_id", nullable = false, precision = 20, comment = "实例的记录id", dataType="bigint")
	private Long instanceId;

	/**
     * 状态: 1:有效，0：无效
     * @definition tinyint(3) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "status", nullable = false, precision = 3, comment = "状态: 1:有效，0：无效", dataType="tinyint")
	private Byte status;

	/**
     * 服务类型；tcp/http
     */
    @ColumnProperty(name = "type", nullable = false, comment = "服务类型：tcp、http", dataType="varchar")
	private String type;
	/**
	 * 流量权重
	 */
	private Float weight;

	public ServiceInstanceRef (){
		super();
	}
	
	public ServiceInstanceRef (ServiceInstanceRef that){
        super(that);
		this.serviceId = that.serviceId;
		this.instanceId = that.instanceId;
		this.status = that.status;
		this.type = that.type;
		this.weight = that.weight;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return ServiceInstanceRefDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = ServiceInstanceRefDefinition.values();
        return defs;
    }
}