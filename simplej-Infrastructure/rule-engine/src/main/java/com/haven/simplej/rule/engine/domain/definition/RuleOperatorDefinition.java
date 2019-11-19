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
import com.haven.simplej.rule.engine.domain.RuleOperator;


/**
 * 规则操作符配置表 字段属性枚举类
 */
public enum RuleOperatorDefinition implements EnumDomainDef {

	id,
	name,
	description,
	returnType,
	dataType,
	opType,
	paramCount,
	paramType,
	label,
	selectedLabel,
	code,
	pluginId,
	status,
	reserved1,
	createTime,
	createdBy,
	updatedBy,
	updateTime,
	isDeleted,
	parserType;

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(RuleOperator.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "rule_operator";
	}
}