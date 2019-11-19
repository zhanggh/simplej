package com.haven.simplej.rpc.center.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.rpc.center.domain.definition.SequenceInfoDefinition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
/**
 * 序列号生成规则信息
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "sequence_info", schema = "rpc_center", indexes = { // catalog 定义为分表数
    @Index(name = "idx_namespace_seq_key", columnList = "namespace, seq_key", unique = true), //
})
public class SequenceInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 所属的命名空间
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "namespace", nullable = false, length = 128, comment = "所属的命名空间", dataType="varchar")
	private String namespace;

	/**
     * 序列号key
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "seq_key", nullable = false, length = 128, comment = "序列号key", dataType="varchar")
	private String seqKey;

	/**
     * 当前序列号
     * @definition bigint(20) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "seq_value", nullable = false, precision = 20, comment = "当前序列号", dataType="bigint")
	private Long seqValue;

	/**
     * 步长
     * @definition int(11) unsigned DEFAULT '100' 
     */
    @ColumnProperty(name = "step", nullable = false, precision = 10, comment = "步长", dataType="int")
	private Integer step;

	/**
     * 序列命名，一般为中文名
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "seq_name", nullable = false, length = 128, comment = "序列命名，一般为中文名", dataType="varchar")
	private String seqName;

	/**
     * 版本号
     * @definition int(11) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "version", nullable = false, precision = 10, comment = "版本号", dataType="int")
	private Integer version;

	public SequenceInfo (){
		super();
	}
	
	public SequenceInfo (SequenceInfo that){
        super(that);
		this.namespace = that.namespace;
		this.seqKey = that.seqKey;
		this.seqValue = that.seqValue;
		this.step = that.step;
		this.seqName = that.seqName;
		this.version = that.version;
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