package com.haven.simplej.authen.domain.definition;
import com.haven.simplej.authen.domain.DepartmentInfo;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;


/**
 * 部门信息 字段属性枚举类
 */
public enum DepartmentInfoDefinition implements EnumDomainDef {

	id,
	code,
	name,
	level,
	remark,
	description,
	managerUserCode,
	telephone,
	isDeleted,
	parentCode,
	createTime,
	createdBy,
	updatedBy,
	updateTime;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(DepartmentInfo.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "department_info";
	}
}