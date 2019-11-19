package com.haven.simplej.authen.domain;
import com.haven.simplej.authen.domain.definition.SequenceInfoDefinition;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
/**
 * 序列号生成器表
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "sequence_info", catalog = "1", schema = "authen", indexes = { // catalog 定义为分表数
    @Index(name = "sequence_idx_key", columnList = "sequence_key", unique = true), //
})
public class SequenceInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 序列名
     * @definition varchar(30) DEFAULT '' 
     */
    @ColumnProperty(name = "sequence_name", nullable = false, length = 30, comment = "序列名", dataType="varchar")
	private String sequenceName;

	/**
     * 序列key
     * @definition varchar(30) DEFAULT '' 
     */
    @ColumnProperty(name = "sequence_key", nullable = false, length = 30, comment = "序列key", dataType="varchar")
	private String sequenceKey;

	/**
     * 当前值
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "current_value", nullable = false, precision = 20, comment = "当前值", dataType="bigint")
	private Long currentValue;

	/**
     * 步长
     * @definition int(11) unsigned DEFAULT '10' 
     */
    @ColumnProperty(name = "step", nullable = false, precision = 10, comment = "步长", dataType="int")
	private Integer step;

	/**
     * 当序列满了之后是否归零开始，0否，1是
     * @definition tinyint(3) unsigned DEFAULT '1' 
     */
    @ColumnProperty(name = "is_cycle", nullable = false, precision = 3, comment = "当序列满了之后是否归零开始，0否，1是", dataType="tinyint")
	private Byte isCycle;

	/**
     * 数据版本号，用于乐观锁
     * @definition int(11) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "data_version", nullable = false, precision = 10, comment = "数据版本号，用于乐观锁", dataType="int")
	private Integer dataVersion;

	public SequenceInfo (){
		super();
	}
	
	public SequenceInfo (SequenceInfo that){
        super(that);
		this.sequenceName = that.sequenceName;
		this.sequenceKey = that.sequenceKey;
		this.currentValue = that.currentValue;
		this.step = that.step;
		this.isCycle = that.isCycle;
		this.dataVersion = that.dataVersion;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return SequenceInfoDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = SequenceInfoDefinition.values();
        return defs;
    }
}