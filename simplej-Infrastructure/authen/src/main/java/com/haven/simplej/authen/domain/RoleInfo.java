package com.haven.simplej.authen.domain;
import com.haven.simplej.authen.domain.definition.RoleInfoDefinition;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 角色信息表
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "role_info", catalog = "1", schema = "authen", indexes = { // catalog 定义为分表数
})
public class RoleInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 角色编号
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "role_code", nullable = false, length = 10, comment = "角色编号", dataType="varchar")
	private String roleCode;

	/**
     * 角色名称
     * @definition varchar(30) DEFAULT '' 
     */
    @ColumnProperty(name = "role_name", nullable = false, length = 30, comment = "角色名称", dataType="varchar")
	private String roleName;

	/**
     * 角色备注
     * @definition varchar(150) DEFAULT '' 
     */
    @ColumnProperty(name = "remark", nullable = false, length = 150, comment = "角色备注", dataType="varchar")
	private String remark;

	/**
     * 所属平台
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "platform", nullable = false, length = 10, comment = "所属平台", dataType="varchar")
	private String platform;

	public RoleInfo (){
		super();
	}
	
	public RoleInfo (RoleInfo that){
        super(that);
		this.roleCode = that.roleCode;
		this.roleName = that.roleName;
		this.remark = that.remark;
		this.platform = that.platform;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return RoleInfoDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = RoleInfoDefinition.values();
        return defs;
    }
}