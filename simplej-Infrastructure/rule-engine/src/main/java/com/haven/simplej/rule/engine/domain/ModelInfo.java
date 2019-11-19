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
import com.haven.simplej.rule.engine.domain.definition.ModelInfoDefinition;
/**
 * 模型信息
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "model_info", catalog = "1", schema = "rule_engine", indexes = { // catalog 定义为分表数
    @Index(name = "idx_en_name", columnList = "en_name", unique = false), //
    @Index(name = "idx_name", columnList = "name", unique = false), //
})
public class ModelInfo extends BaseDomain {
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
     * model groovy 代码
     * @definition text DEFAULT NULL 
     */
    @ColumnProperty(name = "code", nullable = true, length = 65535, comment = "model groovy 代码", dataType="text")
	private String code;

	/**
     * 模型所属的业务场景的id
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "biz_id", nullable = false, precision = 20, comment = "模型所属的业务场景的id", dataType="bigint")
	private Long bizId;

	public ModelInfo (){
		super();
	}
	
	public ModelInfo (ModelInfo that){
        super(that);
		this.name = that.name;
		this.enName = that.enName;
		this.code = that.code;
		this.bizId = that.bizId;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return ModelInfoDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = ModelInfoDefinition.values();
        return defs;
    }
}