package com.haven.simplej.db.base;

import com.haven.simplej.db.annotation.ColumnProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * domain公共属性
 * @author haven.zhang
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseDomain implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键id */
	@Id
	@ColumnProperty(name = "id", nullable = false, precision = 20,comment = "主键id",dataType = "bigint")
    private Long id;
	/** 是否软删除标志 */
	@ColumnProperty(name = "is_deleted", nullable = false, precision = 20,comment = "是否软删除标志",dataType = "tinyint")
	private Byte isDeleted;
	/**
	 * 创建时间
	 */
	@ColumnProperty(name = "create_time", nullable = false,comment = "创建时间",dataType = "timestamp")
	private Timestamp createTime;

	/**
	 * 更新时间
	 */
	@ColumnProperty(name = "update_time", nullable = false,comment = "更新时间",dataType = "timestamp")
	private Timestamp updateTime;

	/**
	 * 创建人
	 */
	@ColumnProperty(name = "created_by", nullable = false,length = 30,comment = "创建人",dataType = "varchar")
	private String createdBy;

	/**
	 * 更新人
	 */
	@ColumnProperty(name = "updated_by", nullable = false,length = 30,comment = "更新人",dataType = "varchar")
	private String updatedBy;


	@ColumnProperty(name = "reserved1", nullable = false,comment = "预留字段1",dataType = "varchar")
	private String reserved1;


	@ColumnProperty(name = "reserved2", nullable = false,comment = "预留字段2",dataType = "varchar")
	private String reserved2;


	protected String tableName;


	public void setTableName(String tableName){
		this.tableName = tableName;
	}
	/**
	 * 获取表名
	 * @return
	 */
	public abstract String getTableName();

	/**
	 * 获取表属性（字段，类型等信息）
	 * @return
	 */
	public abstract EnumDomainDef[] getFields();

	public BaseDomain() {
		super();
	}

	public BaseDomain(Long id) {
		this();
		this.id = id;
	}

	public BaseDomain(BaseDomain that) {
		this.id = that.id;
		this.isDeleted = that.isDeleted;
		this.createTime = that.createTime;
		this.createdBy = that.createdBy;
		this.updateTime = that.updateTime;
		this.updatedBy = that.updatedBy;
		this.reserved1 = that.reserved1;
		this.reserved2 = that.reserved2;
	}



	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
