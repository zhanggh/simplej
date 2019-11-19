package com.haven.simplej.authen.domain;
import com.haven.simplej.authen.domain.definition.DepartmentInfoDefinition;
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
 * 部门信息
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "department_info", catalog = "1", schema = "authen", indexes = { // catalog 定义为分表数
    @Index(name = "department_idx_code", columnList = "code", unique = true), //
})
public class DepartmentInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 部门编号
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "code", nullable = false, length = 10, comment = "部门编号", dataType="varchar")
	private String code;

	/**
     * 部门名称
     * @definition varchar(30) DEFAULT '' 
     */
    @ColumnProperty(name = "name", nullable = false, length = 30, comment = "部门名称", dataType="varchar")
	private String name;

	/**
     * 部门级别，1：一级部门，2：二级部门
     * @definition tinyint(3) unsigned DEFAULT '1' 
     */
    @ColumnProperty(name = "level", nullable = false, precision = 3, comment = "部门级别，1：一级部门，2：二级部门", dataType="tinyint")
	private Byte level;

	/**
     * 备注
     * @definition varchar(150) DEFAULT '' 
     */
    @ColumnProperty(name = "remark", nullable = false, length = 150, comment = "备注", dataType="varchar")
	private String remark;

	/**
     * 部门描述
     * @definition varchar(150) DEFAULT '' 
     */
    @ColumnProperty(name = "description", nullable = false, length = 150, comment = "部门描述", dataType="varchar")
	private String description;

	/**
     * 部门负责人用户编号
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "manager_user_code", nullable = false, length = 10, comment = "部门负责人用户编号", dataType="varchar")
	private String managerUserCode;

	/**
     * 部门固话
     * @definition varchar(20) DEFAULT '' 
     */
    @ColumnProperty(name = "telephone", nullable = false, length = 20, comment = "部门固话", dataType="varchar")
	private String telephone;

	/**
     * 上级部门编号
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "parent_code", nullable = false, length = 10, comment = "上级部门编号", dataType="varchar")
	private String parentCode;

	public DepartmentInfo (){
		super();
	}
	
	public DepartmentInfo (DepartmentInfo that){
        super(that);
		this.code = that.code;
		this.name = that.name;
		this.level = that.level;
		this.remark = that.remark;
		this.description = that.description;
		this.managerUserCode = that.managerUserCode;
		this.telephone = that.telephone;
		this.parentCode = that.parentCode;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return DepartmentInfoDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = DepartmentInfoDefinition.values();
        return defs;
    }
}