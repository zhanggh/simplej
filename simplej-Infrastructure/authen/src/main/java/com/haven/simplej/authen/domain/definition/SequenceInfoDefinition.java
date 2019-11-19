package com.haven.simplej.authen.domain.definition;
import com.haven.simplej.authen.domain.SequenceInfo;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;


/**
 * 序列号生成器表 字段属性枚举类
 */
public enum SequenceInfoDefinition implements EnumDomainDef {

	id,
	sequenceName,
	sequenceKey,
	currentValue,
	step,
	isCycle,
	dataVersion,
	createTime,
	createdBy,
	updateTime,
	updatedBy;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(SequenceInfo.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "sequence_info";
	}
}