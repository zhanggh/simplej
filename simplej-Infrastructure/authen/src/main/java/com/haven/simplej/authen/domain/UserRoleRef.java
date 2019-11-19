package com.haven.simplej.authen.domain;
import com.haven.simplej.authen.domain.definition.UserRoleRefDefinition;
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
 * 用户角色关联表
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "user_role_ref", catalog = "1", schema = "authen", indexes = { // catalog 定义为分表数
    @Index(name = "user_role_idx_user_code_role_code_platform", columnList = "user_code, role_code, platform", unique = false), //
})
public class UserRoleRef extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 用户号
     * @definition varchar(30) DEFAULT '' 
     */
    @ColumnProperty(name = "user_code", nullable = false, length = 30, comment = "用户号", dataType="varchar")
	private String userCode;

	/**
     * 角色号
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "role_code", nullable = false, length = 10, comment = "角色号", dataType="varchar")
	private String roleCode;

	/**
     * 所属平台标志
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "platform", nullable = false, length = 10, comment = "所属平台标志", dataType="varchar")
	private String platform;

	/**
     * 备注
     * @definition varchar(150) DEFAULT '' 
     */
    @ColumnProperty(name = "remark", nullable = false, length = 150, comment = "备注", dataType="varchar")
	private String remark;

	public UserRoleRef (){
		super();
	}
	
	public UserRoleRef (UserRoleRef that){
        super(that);
		this.userCode = that.userCode;
		this.roleCode = that.roleCode;
		this.platform = that.platform;
		this.remark = that.remark;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return UserRoleRefDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = UserRoleRefDefinition.values();
        return defs;
    }
}