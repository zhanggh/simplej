package com.haven.simplej.authen.domain;
import com.haven.simplej.authen.domain.definition.RoleMenuRefDefinition;
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
 * 角色与菜单关联表
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "role_menu_ref", catalog = "1", schema = "authen", indexes = { // catalog 定义为分表数
    @Index(name = "role_menu_idx_role_code_menu_code", columnList = "role_code, menu_code", unique = false), //
})
public class RoleMenuRef extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 角色编号
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "role_code", nullable = false, length = 10, comment = "角色编号", dataType="varchar")
	private String roleCode;

	/**
     * 菜单编号
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "menu_code", nullable = false, length = 10, comment = "菜单编号", dataType="varchar")
	private String menuCode;

	/**
     * 备注
     * @definition varchar(150) DEFAULT '' 
     */
    @ColumnProperty(name = "remark", nullable = false, length = 150, comment = "备注", dataType="varchar")
	private String remark;

	public RoleMenuRef (){
		super();
	}
	
	public RoleMenuRef (RoleMenuRef that){
        super(that);
		this.roleCode = that.roleCode;
		this.menuCode = that.menuCode;
		this.remark = that.remark;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return RoleMenuRefDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = RoleMenuRefDefinition.values();
        return defs;
    }
}