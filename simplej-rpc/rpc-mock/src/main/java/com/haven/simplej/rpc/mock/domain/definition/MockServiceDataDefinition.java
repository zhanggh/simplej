package com.haven.simplej.rpc.mock.domain.definition;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;
import com.haven.simplej.rpc.mock.domain.MockServiceData;


/**
 * 远程服务方法模拟结果信息 字段属性枚举类
 */
public enum MockServiceDataDefinition implements EnumDomainDef {

	id,
	timeout,
	namespace,
	serviceName,
	methodName,
	methodId,
	paramType,
	returnType,
	response,
	rule,
	createTime,
	createdBy,
	updatedBy,
	updateTime,
	isDeleted,
	version,
	effectiveTime,
	expireTime,
	status;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(MockServiceData.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "rpc_mock.mock_service_data";
	}
}