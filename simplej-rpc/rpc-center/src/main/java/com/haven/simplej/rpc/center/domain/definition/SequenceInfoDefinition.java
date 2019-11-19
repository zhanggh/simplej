package com.haven.simplej.rpc.center.domain.definition;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;
import com.haven.simplej.rpc.center.domain.SequenceInfo;


/**
 * 序列号生成规则信息 字段属性枚举类
 */
public enum SequenceInfoDefinition implements EnumDomainDef {

	id,
	namespace,
	seqKey,
	seqValue,
	step,
	seqName,
	reserved1,
	createTime,
	createdBy,
	updatedBy,
	updateTime,
	isDeleted,
	version;

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
		return "rpc_center.sequence_info";
	}
}