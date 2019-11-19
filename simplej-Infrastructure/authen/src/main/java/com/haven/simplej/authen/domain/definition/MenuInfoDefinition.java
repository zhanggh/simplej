package com.haven.simplej.authen.domain.definition;
import com.haven.simplej.authen.domain.MenuInfo;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;


/**
 * 菜单信息表 字段属性枚举类
 */
public enum MenuInfoDefinition implements EnumDomainDef {

	id,
	menuCode,
	menuName,
	menuLevel,
	parentMenuCode,
	menuUri,
	remark,
	menuStatus,
	orderNum,
	platform,
	menuType,
	isDeleted,
	createTime,
	createdBy,
	updatedBy,
	icon,
	updateTime;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(MenuInfo.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "menu_info";
	}
}