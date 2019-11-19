package com.haven.simplej.security.domain.definition;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;
import com.haven.simplej.security.domain.AppCertRef;


/**
 * 接入方对应的证书关联管理表 字段属性枚举类
 */
public enum AppCertRefDefinition implements EnumDomainDef {

	id,
	appId,
	certId,
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
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(AppCertRef.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "app_cert_ref";
	}
}