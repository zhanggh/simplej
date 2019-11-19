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
import com.haven.simplej.rule.engine.domain.definition.PluginItemDefinition;
/**
 * 插件信息，注意，一个插件仅提供一个方法
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "plugin_item", catalog = "1", schema = "rule_engine", indexes = { // catalog 定义为分表数
    @Index(name = "idx_name", columnList = "name", unique = false), //
})
public class PluginItem extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 插件名称
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "name", nullable = false, length = 128, comment = "插件名称", dataType="varchar")
	private String name;

	/**
     * 插件描述
     * @definition varchar(512) DEFAULT '' 
     */
    @ColumnProperty(name = "description", nullable = false, length = 512, comment = "插件描述", dataType="varchar")
	private String description;

	/**
     * plugin groovy 代码
     * @definition text DEFAULT NULL 
     */
    @ColumnProperty(name = "code", nullable = true, length = 65535, comment = "plugin groovy 代码", dataType="text")
	private String code;

	/**
     * 入参
     * @definition varchar(512) DEFAULT '' 
     */
    @ColumnProperty(name = "input_params", nullable = false, length = 512, comment = "入参", dataType="varchar")
	private String inputParams;

	/**
     * 返回值类型
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "return_type", nullable = false, length = 128, comment = "返回值类型", dataType="varchar")
	private String returnType;

	/**
     * 状态: 1:有效，0：无效
     * @definition tinyint(3) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "status", nullable = false, precision = 3, comment = "状态: 1:有效，0：无效", dataType="tinyint")
	private Byte status;

	public PluginItem (){
		super();
	}
	
	public PluginItem (PluginItem that){
        super(that);
		this.name = that.name;
		this.description = that.description;
		this.code = that.code;
		this.inputParams = that.inputParams;
		this.returnType = that.returnType;
		this.status = that.status;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return PluginItemDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = PluginItemDefinition.values();
        return defs;
    }
}