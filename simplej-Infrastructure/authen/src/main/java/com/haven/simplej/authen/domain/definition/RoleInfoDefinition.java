package com.haven.simplej.authen.domain.definition;
import com.haven.simplej.authen.domain.RoleInfo;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;


/**
 * 角色信息表 字段属性枚举类
 */
public enum RoleInfoDefinition implements EnumDomainDef {

	id,
	roleCode,
	roleName,
	remark,
	platform,
	isDeleted,
	createdBy,
	updatedBy,
	createTime,
	updateTime;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(RoleInfo.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "role_info";
	}
}