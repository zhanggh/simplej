package com.haven.simplej.authen.domain;
import com.haven.simplej.authen.domain.definition.MenuInfoDefinition;
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
 * 菜单信息表
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "menu_info", catalog = "1", schema = "authen", indexes = { // catalog 定义为分表数
    @Index(name = "menu_idx_menu_code", columnList = "menu_code", unique = true), //
    @Index(name = "menu_idx_platform_level", columnList = "menu_level, platform", unique = false), //
})
public class MenuInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 菜单编号
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "menu_code", nullable = false, length = 10, comment = "菜单编号", dataType="varchar")
	private String menuCode;

	/**
     * 菜单名
     * @definition varchar(50) DEFAULT '' 
     */
    @ColumnProperty(name = "menu_name", nullable = false, length = 50, comment = "菜单名", dataType="varchar")
	private String menuName;

	/**
     * 菜单级别，1：一级菜单，2：二级菜单，3：三级菜单，4：四级菜单
     * @definition tinyint(3) unsigned DEFAULT '1' 
     */
    @ColumnProperty(name = "menu_level", nullable = false, precision = 3, comment = "菜单级别，1：一级菜单，2：二级菜单，3：三级菜单，4：四级菜单", dataType="tinyint")
	private Byte menuLevel;

	/**
     * 父级菜单编号
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "parent_menu_code", nullable = false, length = 10, comment = "父级菜单编号", dataType="varchar")
	private String parentMenuCode;

	/**
     * 菜单uri
     * @definition varchar(60) DEFAULT '' 
     */
    @ColumnProperty(name = "menu_uri", nullable = false, length = 60, comment = "菜单uri", dataType="varchar")
	private String menuUri;

	/**
     * 菜单描述
     * @definition varchar(150) DEFAULT '' 
     */
    @ColumnProperty(name = "remark", nullable = false, length = 150, comment = "菜单描述", dataType="varchar")
	private String remark;

	/**
     * 菜单状态，1：启用，0禁用
     * @definition tinyint(3) unsigned DEFAULT '1' 
     */
    @ColumnProperty(name = "menu_status", nullable = false, precision = 3, comment = "菜单状态，1：启用，0禁用", dataType="tinyint")
	private Byte menuStatus;

	/**
     * 菜单顺序，数值越小，排序越前面
     * @definition tinyint(3) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "order_num", nullable = false, precision = 3, comment = "菜单顺序，数值越小，排序越前面", dataType="tinyint")
	private Byte orderNum;

	/**
     * 所属平台
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "platform", nullable = false, length = 10, comment = "所属平台", dataType="varchar")
	private String platform;

	/**
     * 菜单类型：0：平台菜单 1：外部地址
     * @definition tinyint(3) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "menu_type", nullable = false, precision = 3, comment = "菜单类型：0：平台菜单 1：外部地址", dataType="tinyint")
	private Byte menuType;

	/**
     * 图标地址
     * @definition varchar(60) DEFAULT '' 
     */
    @ColumnProperty(name = "icon", nullable = false, length = 60, comment = "图标地址", dataType="varchar")
	private String icon;

	public MenuInfo (){
		super();
	}
	
	public MenuInfo (MenuInfo that){
        super(that);
		this.menuCode = that.menuCode;
		this.menuName = that.menuName;
		this.menuLevel = that.menuLevel;
		this.parentMenuCode = that.parentMenuCode;
		this.menuUri = that.menuUri;
		this.remark = that.remark;
		this.menuStatus = that.menuStatus;
		this.orderNum = that.orderNum;
		this.platform = that.platform;
		this.menuType = that.menuType;
		this.icon = that.icon;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return MenuInfoDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = MenuInfoDefinition.values();
        return defs;
    }
}