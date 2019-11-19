package com.haven.simplej.rpc.registry.domain.definition;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;
import com.haven.simplej.rpc.registry.domain.ServiceInfo;


/**
 * rpc服务信息 字段属性枚举类
 */
public enum ServiceInfoDefinition implements EnumDomainDef {

	id,
	namespace,
	serviceName,
	version,
	status,
	serviceVersion,
	methodInfo,
	serverType,
	remark,
	timeout,
	isDeleted,
	createTime,
	createdBy,
	updatedBy,
	updateTime,
	heartbeatTime;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(ServiceInfo.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "rpc_register.service_info";
	}
}