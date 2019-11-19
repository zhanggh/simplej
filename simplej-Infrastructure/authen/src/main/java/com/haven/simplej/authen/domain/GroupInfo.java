package com.haven.simplej.authen.domain;
import com.haven.simplej.authen.domain.definition.GroupInfoDefinition;
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
 * 小组信息，一个部门下面对应多个小组
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "group_info", catalog = "1", schema = "authen", indexes = { // catalog 定义为分表数
    @Index(name = "group_idx_code", columnList = "group_code", unique = true), //
})
public class GroupInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 小组编号
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "group_code", nullable = false, length = 10, comment = "小组编号", dataType="varchar")
	private String groupCode;

	/**
     * 小组名
     * @definition varchar(50) DEFAULT '' 
     */
    @ColumnProperty(name = "group_name", nullable = false, length = 50, comment = "小组名", dataType="varchar")
	private String groupName;

	/**
     * 小组级别
     * @definition tinyint(3) unsigned DEFAULT '1' 
     */
    @ColumnProperty(name = "level", nullable = false, precision = 3, comment = "小组级别", dataType="tinyint")
	private Byte level;

	/**
     * 小组负责人用户编号
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "group_manager_code", nullable = false, length = 10, comment = "小组负责人用户编号", dataType="varchar")
	private String groupManagerCode;

	/**
     * 小组负责内容描述
     * @definition varchar(150) DEFAULT '' 
     */
    @ColumnProperty(name = "description", nullable = false, length = 150, comment = "小组负责内容描述", dataType="varchar")
	private String description;

	/**
     * 所属部门编号
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "department_code", nullable = false, length = 10, comment = "所属部门编号", dataType="varchar")
	private String departmentCode;

	/**
     * 备注
     * @definition varchar(150) DEFAULT '' 
     */
    @ColumnProperty(name = "remark", nullable = false, length = 150, comment = "备注", dataType="varchar")
	private String remark;

	/**
     * 上级小组编号
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "parent_code", nullable = false, length = 10, comment = "上级小组编号", dataType="varchar")
	private String parentCode;

	public GroupInfo (){
		super();
	}
	
	public GroupInfo (GroupInfo that){
        super(that);
		this.groupCode = that.groupCode;
		this.groupName = that.groupName;
		this.level = that.level;
		this.groupManagerCode = that.groupManagerCode;
		this.description = that.description;
		this.departmentCode = that.departmentCode;
		this.remark = that.remark;
		this.parentCode = that.parentCode;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return GroupInfoDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = GroupInfoDefinition.values();
        return defs;
    }
}