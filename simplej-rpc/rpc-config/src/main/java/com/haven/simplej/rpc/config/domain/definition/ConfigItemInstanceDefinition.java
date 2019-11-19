package com.haven.simplej.rpc.config.domain.definition;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;
import com.haven.simplej.rpc.config.domain.ConfigItemInstance;


/**
 * 配置项生效的服务对应的实例 字段属性枚举类
 */
public enum ConfigItemInstanceDefinition implements EnumDomainDef {

	id,
	cfgId,
	host,
	port,
	httpPort,
	status,
	createTime,
	createdBy,
	updatedBy,
	updateTime,
	isDeleted;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(ConfigItemInstance.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "rpc_config.config_item_instance";
	}
}