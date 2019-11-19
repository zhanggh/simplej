package com.haven.simplej.rpc.registry.domain.definition;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;
import com.haven.simplej.rpc.registry.domain.ServiceInstanceRef;


/**
 * 服务与实例的关联关系 字段属性枚举类
 */
public enum ServiceInstanceRefDefinition implements EnumDomainDef {

	id,
	serviceId,
	instanceId,
	reserved1,
	status,
	createTime,
	createdBy,
	updatedBy,
	updateTime,
	isDeleted,
	type,
	weight;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(ServiceInstanceRef.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "service_instance_ref";
	}
}