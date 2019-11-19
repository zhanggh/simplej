package com.haven.simplej.rule.engine.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import lombok.*;
import com.haven.simplej.db.annotation.*;
import java.sql.*;
import javax.persistence.*;
import java.math.BigDecimal;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import org.springframework.format.annotation.DateTimeFormat;
import org.apache.commons.lang3.StringUtils;
import com.haven.simplej.rule.engine.domain.definition.RuleOperatorDefinition;
/**
 * 规则操作符配置表
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "rule_operator", catalog = "1", schema = "rule_engine", indexes = { // catalog 定义为分表数
    @Index(name = "name_op_type", columnList = "name, op_type", unique = true), //
})
public class RuleOperator extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 操作符名称
     * @definition varchar(32) DEFAULT '' 
     */
    @ColumnProperty(name = "name", nullable = false, length = 32, comment = "操作符名称", dataType="varchar")
	private String name;

	/**
     * 描述
     * @definition varchar(255) DEFAULT '' 
     */
    @ColumnProperty(name = "description", nullable = false, length = 255, comment = "描述", dataType="varchar")
	private String description;

	/**
     * 操作结果返回数据类型
     * @definition varchar(16) DEFAULT '' 
     */
    @ColumnProperty(name = "return_type", nullable = false, length = 16, comment = "操作结果返回数据类型", dataType="varchar")
	private String returnType;

	/**
     * 适用的数据类型 1:字符串; 2:数字; 3:日期; 4:布尔; 5:字典; 0:其他
     * @definition tinyint(3) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "data_type", nullable = false, precision = 3, comment = "适用的数据类型 1:字符串; 2:数字; 3:日期; 4:布尔; 5:字典; 0:其他", dataType="tinyint")
	private Byte dataType;

	/**
     * 操作符类型：1-算术运算符，2-逻辑运算符，3-比较运算符，4-移位运算符，5-括号,6-赋值运算符，7-插件运算符
     * @definition tinyint(3) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "op_type", nullable = false, precision = 3, comment = "操作符类型：1-算术运算符，2-逻辑运算符，3-比较运算符，4-移位运算符，5-括号,6-赋值运算符，7-插件运算符", dataType="tinyint")
	private Byte opType;

	/**
     * 操作符后面的参数数量
     * @definition tinyint(3) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "param_count", nullable = false, precision = 3, comment = "操作符后面的参数数量", dataType="tinyint")
	private Byte paramCount;

	/**
     * 参数类型的配置, json格式
     * @definition varchar(512) DEFAULT '' 
     */
    @ColumnProperty(name = "param_type", nullable = false, length = 512, comment = "参数类型的配置, json格式", dataType="varchar")
	private String paramType;

	/**
     * 比较符的中文描述
     * @definition varchar(256) DEFAULT '' 
     */
    @ColumnProperty(name = "label", nullable = false, length = 256, comment = "比较符的中文描述", dataType="varchar")
	private String label;

	/**
     * 选中后的显示的描述
     * @definition varchar(256) DEFAULT '' 
     */
    @ColumnProperty(name = "selected_label", nullable = false, length = 256, comment = "选中后的显示的描述", dataType="varchar")
	private String selectedLabel;

	/**
     * 比较符生成的可执行代码, 包含占位符用于填充对象和参数
     * @definition varchar(256) DEFAULT '' 
     */
    @ColumnProperty(name = "code", nullable = false, length = 256, comment = "比较符生成的可执行代码, 包含占位符用于填充对象和参数", dataType="varchar")
	private String code;

	/**
     * 执行所需的插件ID, 如使用自定义方法则关联插件表
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "plugin_id", nullable = false, precision = 20, comment = "执行所需的插件ID, 如使用自定义方法则关联插件表", dataType="bigint")
	private Long pluginId;

	/**
     * 状态: 0:创建;1:删除
     * @definition tinyint(3) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "status", nullable = false, precision = 3, comment = "状态: 0:创建;1:删除", dataType="tinyint")
	private Byte status;

	/**
     * 解析引擎：el/groovy
     * @definition varchar(16) DEFAULT ''el'' 
     */
    @ColumnProperty(name = "parser_type", nullable = false, length = 16, comment = "解析引擎：el/groovy", dataType="varchar")
	private String parserType;

	public RuleOperator (){
		super();
	}
	
	public RuleOperator (RuleOperator that){
        super(that);
		this.name = that.name;
		this.description = that.description;
		this.returnType = that.returnType;
		this.dataType = that.dataType;
		this.opType = that.opType;
		this.paramCount = that.paramCount;
		this.paramType = that.paramType;
		this.label = that.label;
		this.selectedLabel = that.selectedLabel;
		this.code = that.code;
		this.pluginId = that.pluginId;
		this.status = that.status;
		this.parserType = that.parserType;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return RuleOperatorDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = RuleOperatorDefinition.values();
        return defs;
    }
}