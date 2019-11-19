package com.haven.simplej.authen.domain;
import com.haven.simplej.authen.domain.definition.OptLogDefinition;
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
 * 操作日志表
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "opt_log", catalog = "1", schema = "authen", indexes = { // catalog 定义为分表数
    @Index(name = "idx_created_by", columnList = "created_by", unique = false), //
    @Index(name = "idx_create_time", columnList = "create_time", unique = false), //
})
public class OptLog extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 业务类型编码
     * @definition varchar(24) DEFAULT '' 
     */
    @ColumnProperty(name = "biz_type", nullable = false, length = 24, comment = "业务类型编码", dataType="varchar")
	private String bizType;

	/**
     * 目标操作表
     * @definition varchar(24) DEFAULT '' 
     */
    @ColumnProperty(name = "dest_table", nullable = false, length = 24, comment = "目标操作表", dataType="varchar")
	private String destTable;

	/**
     * 操作人
     * @definition varchar(24) DEFAULT '' 
     */
    @ColumnProperty(name = "operator", nullable = false, length = 24, comment = "操作人", dataType="varchar")
	private String operator;

	/**
     * 操作类型：query/update/insert/delete
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "op_type", nullable = false, length = 10, comment = "操作类型：query/update/insert/delete", dataType="varchar")
	private String opType;

	/**
     * 入参
     * @definition text DEFAULT NULL 
     */
    @ColumnProperty(name = "input", nullable = false, length = 65535, comment = "入参", dataType="text")
	private String input;

	/**
     * 用户角色
     * @definition varchar(20) DEFAULT '' 
     */
    @ColumnProperty(name = "user_role", nullable = false, length = 20, comment = "用户角色", dataType="varchar")
	private String userRole;

	/**
     * 保留字段，一般不建议使用
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "reserved", nullable = false, length = 128, comment = "保留字段，一般不建议使用", dataType="varchar")
	private String reserved;

	public OptLog (){
		super();
	}
	
	public OptLog (OptLog that){
        super(that);
		this.bizType = that.bizType;
		this.destTable = that.destTable;
		this.operator = that.operator;
		this.opType = that.opType;
		this.input = that.input;
		this.userRole = that.userRole;
		this.reserved = that.reserved;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return OptLogDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = OptLogDefinition.values();
        return defs;
    }
}