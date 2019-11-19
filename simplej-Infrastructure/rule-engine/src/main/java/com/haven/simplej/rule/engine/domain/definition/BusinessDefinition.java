package com.haven.simplej.rule.engine.domain.definition;
import com.haven.simplej.db.annotation.ColumnProperty;
import lombok.*;
import com.haven.simplej.db.annotation.*;
import java.sql.*;
import javax.persistence.*;
import java.math.BigDecimal;
import com.haven.simplej.db.base.BaseDomain;
import org.springframework.format.annotation.DateTimeFormat;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.util.DomainUtil;
import com.haven.simplej.rule.engine.domain.Business;


/**
 * 接入方信息 字段属性枚举类
 */
public enum BusinessDefinition implements EnumDomainDef {

	id,
	appId,
	name,
	description,
	reserved1,
	status,
	createTime,
	createdBy,
	updatedBy,
	updateTime,
	isDeleted;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(Business.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "business";
	}
}