package com.haven.simplej.rpc.registry.domain.definition;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;
import com.haven.simplej.rpc.registry.domain.UrlServiceInfo;


/**
 * web服务url信息 字段属性枚举类
 */
public enum UrlInfoDefinition implements EnumDomainDef {

	id,
	uri,
	method,
	header,
	urlSchema,
	status,
	namespace,
	reserved1,
	timeout,
	urlVersion,
	createTime,
	createdBy,
	updatedBy,
	updateTime,
	heartbeatTime,
	isDeleted;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(UrlServiceInfo.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "rpc_register.url_info";
	}
}