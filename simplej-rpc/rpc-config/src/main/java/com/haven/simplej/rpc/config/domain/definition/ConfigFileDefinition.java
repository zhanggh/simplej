package com.haven.simplej.rpc.config.domain.definition;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;
import com.haven.simplej.rpc.config.domain.ConfigFile;


/**
 * 属性文件信息 字段属性枚举类
 */
public enum ConfigFileDefinition implements EnumDomainDef {

	id,
	namespace,
	fileName,
	fileContext,
	fileType,
	reserved1,
	createTime,
	createdBy,
	updatedBy,
	updateTime,
	isDeleted,
	fileVersion,
	description,
	status,
	effectiveTime,
	expireTime;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(ConfigFile.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "rpc_config.config_file";
	}
}