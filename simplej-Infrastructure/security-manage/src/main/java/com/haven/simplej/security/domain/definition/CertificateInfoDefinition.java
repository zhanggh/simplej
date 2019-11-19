package com.haven.simplej.security.domain.definition;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;
import com.haven.simplej.security.domain.CertificateInfo;


/**
 * 接入方对应的key关联管理表 字段属性枚举类
 */
public enum CertificateInfoDefinition implements EnumDomainDef {

	id,
	certSequence,
	format,
	keyId,
	certType,
	certContext,
	createTime,
	updateTime,
	createdBy,
	updatedBy,
	isDeleted,
	reserved;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(CertificateInfo.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "certificate_info";
	}
}