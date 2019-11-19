package com.haven.simplej.security.domain.definition;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;
import com.haven.simplej.security.domain.KeyInfo;


/**
 * 秘钥信息表 字段属性枚举类
 */
public enum KeyInfoDefinition implements EnumDomainDef {

	id,
	keyType,
	iv,
	mode,
	version,
	keyName,
	key,
	business,
	createTime,
	updateTime,
	createdBy,
	updatedBy,
	isDeleted,
	reserved;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(KeyInfo.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "key_info";
	}
}