package com.haven.simplej.authen.domain.definition;
import com.haven.simplej.authen.domain.GroupInfo;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;


/**
 * 小组信息，一个部门下面对应多个小组 字段属性枚举类
 */
public enum GroupInfoDefinition implements EnumDomainDef {

	id,
	groupCode,
	groupName,
	level,
	groupManagerCode,
	description,
	departmentCode,
	remark,
	isDeleted,
	parentCode,
	createTime,
	createdBy,
	updatedBy,
	updateTime;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(GroupInfo.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "group_info";
	}
}