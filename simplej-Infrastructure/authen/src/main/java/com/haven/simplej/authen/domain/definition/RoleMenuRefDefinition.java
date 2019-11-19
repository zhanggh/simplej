package com.haven.simplej.authen.domain.definition;
import com.haven.simplej.authen.domain.RoleMenuRef;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;


/**
 * 角色与菜单关联表 字段属性枚举类
 */
public enum RoleMenuRefDefinition implements EnumDomainDef {

	id,
	roleCode,
	menuCode,
	remark,
	isDeleted,
	createTime,
	createdBy,
	updateTime,
	updatedBy;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(RoleMenuRef.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "role_menu_ref";
	}
}