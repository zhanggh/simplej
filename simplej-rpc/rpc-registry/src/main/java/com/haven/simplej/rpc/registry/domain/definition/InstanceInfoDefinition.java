package com.haven.simplej.rpc.registry.domain.definition;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;
import com.haven.simplej.rpc.registry.domain.InstanceInfo;


/**
 * rpc服务的实例信息 字段属性枚举类
 */
public enum InstanceInfoDefinition implements EnumDomainDef {

	id,
	host,
	port,
	status,
	idc,
	namespace,
	remark,
	isDeleted,
	createTime,
	createdBy,
	updatedBy,
	heartbeatTime,
	updateTime,
	weight,
	regionId,
	proxyPort,
	httpPort,
	proxyHttpPort;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(InstanceInfo.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "rpc_register.instance_info";
	}
}