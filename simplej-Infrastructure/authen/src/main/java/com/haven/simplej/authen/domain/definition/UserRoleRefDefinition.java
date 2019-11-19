package com.haven.simplej.authen.domain.definition;
import com.haven.simplej.authen.domain.UserRoleRef;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;


/**
 * 用户角色关联表 字段属性枚举类
 */
public enum UserRoleRefDefinition implements EnumDomainDef {

	id,
	userCode,
	roleCode,
	platform,
	remark,
	isDeleted,
	createTime,
	createdBy,
	updatedBy,
	updateTime;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(UserRoleRef.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "user_role_ref";
	}
}