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
import com.haven.simplej.rule.engine.domain.definition.ModelFieldDefinition;
/**
 * 模型字段信息，与model_info表是多对一关系
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "model_field", catalog = "1", schema = "rule_engine", indexes = { // catalog 定义为分表数
    @Index(name = "idx_en_name", columnList = "en_name", unique = false), //
    @Index(name = "idx_name", columnList = "name", unique = false), //
})
public class ModelField extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 模型名称，中文
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "name", nullable = false, length = 128, comment = "模型名称，中文", dataType="varchar")
	private String name;

	/**
     * 英文名称
     * @definition varchar(32) DEFAULT '' 
     */
    @ColumnProperty(name = "en_name", nullable = false, length = 32, comment = "英文名称", dataType="varchar")
	private String enName;

	/**
     * 数据类型，如：java.lang.int
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "field_type", nullable = false, length = 128, comment = "数据类型，如：java.lang.int", dataType="varchar")
	private String fieldType;

	/**
     * 所属model的id
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "model_id", nullable = false, precision = 20, comment = "所属model的id", dataType="bigint")
	private Long modelId;

	public ModelField (){
		super();
	}
	
	public ModelField (ModelField that){
        super(that);
		this.name = that.name;
		this.enName = that.enName;
		this.fieldType = that.fieldType;
		this.modelId = that.modelId;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return ModelFieldDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = ModelFieldDefinition.values();
        return defs;
    }
}