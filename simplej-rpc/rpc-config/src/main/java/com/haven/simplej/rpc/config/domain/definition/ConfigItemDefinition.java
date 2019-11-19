package com.haven.simplej.rpc.config.domain.definition;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;
import com.haven.simplej.rpc.config.domain.ConfigItem;


/**
 * 属性配置信息 字段属性枚举类
 */
public enum ConfigItemDefinition implements EnumDomainDef {

	id,
	namespace,
	itemName,
	itemValue,
	itemType,
	reserved1,
	createTime,
	createdBy,
	updatedBy,
	updateTime,
	isDeleted,
	itemVersion,
	description,
	status,
	effectiveTime,
	expireTime;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(ConfigItem.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "rpc_config.config_item";
	}
}