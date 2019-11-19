package com.haven.simplej.authen.domain;
import com.haven.simplej.authen.domain.definition.UserInfoDefinition;
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
import java.math.BigDecimal;
/**
 * 用户信息表
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "user_info", catalog = "1", schema = "authen", indexes = { // catalog 定义为分表数
    @Index(name = "user_info_idx_user_code", columnList = "user_code", unique = true), //
    @Index(name = "user_info_idx_user_name", columnList = "user_name", unique = false), //
})
public class UserInfo extends BaseDomain {
	private static final long serialVersionUID = 1L;

	/**
     * 用户登录号
     * @definition varchar(30) DEFAULT '' 
     */
    @ColumnProperty(name = "user_code", nullable = false, length = 30, comment = "用户登录号", dataType="varchar")
	private String userCode;

	/**
     * 用户名称
     * @definition varchar(60) DEFAULT '' 
     */
    @ColumnProperty(name = "user_name", nullable = false, length = 60, comment = "用户名称", dataType="varchar")
	private String userName;

	/**
     * 用户密码，经过加密后存储
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "user_passwd", nullable = false, length = 128, comment = "用户密码，经过加密后存储", dataType="varchar")
	private String userPasswd;

	/**
     * 微信号
     * @definition varchar(30) DEFAULT '' 
     */
    @ColumnProperty(name = "weixin_no", nullable = false, length = 30, comment = "微信号", dataType="varchar")
	private String weixinNo;

	/**
     * 手机号
     * @definition varchar(11) DEFAULT '' 
     */
    @ColumnProperty(name = "mobile", nullable = false, length = 11, comment = "手机号", dataType="varchar")
	private String mobile;

	/**
     * qq号码
     * @definition varchar(15) DEFAULT '' 
     */
    @ColumnProperty(name = "qq_no", nullable = false, length = 15, comment = "qq号码", dataType="varchar")
	private String qqNo;

	/**
     * 固话号码
     * @definition varchar(20) DEFAULT '' 
     */
    @ColumnProperty(name = "telephone", nullable = false, length = 20, comment = "固话号码", dataType="varchar")
	private String telephone;

	/**
     * 邮件地址
     * @definition varchar(40) DEFAULT '' 
     */
    @ColumnProperty(name = "email", nullable = false, length = 40, comment = "邮件地址", dataType="varchar")
	private String email;

	/**
     * 性别：0男性，1女生
     * @definition tinyint(1) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "sex", nullable = false, precision = 3, comment = "性别：0男性，1女生", dataType="tinyint")
	private Byte sex;

	/**
     * 年龄
     * @definition tinyint(3) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "age", nullable = false, precision = 3, comment = "年龄", dataType="tinyint")
	private Byte age;

	/**
     * 所在省份
     * @definition varchar(10) DEFAULT '' 
     */
    @ColumnProperty(name = "province", nullable = false, length = 10, comment = "所在省份", dataType="varchar")
	private String province;

	/**
     * 所在城市
     * @definition varchar(20) DEFAULT '' 
     */
    @ColumnProperty(name = "city", nullable = false, length = 20, comment = "所在城市", dataType="varchar")
	private String city;

	/**
     * 详细地址
     * @definition varchar(128) DEFAULT '' 
     */
    @ColumnProperty(name = "address", nullable = false, length = 128, comment = "详细地址", dataType="varchar")
	private String address;

	/**
     * 备注信息
     * @definition varchar(150) DEFAULT '' 
     */
    @ColumnProperty(name = "remark", nullable = false, length = 150, comment = "备注信息", dataType="varchar")
	private String remark;

	/**
     * 0:小学 1：初中  2：高中  3：本科 4：硕士 5博士 6：其他
     * @definition tinyint(1) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "education", nullable = false, precision = 3, comment = "0:小学 1：初中  2：高中  3：本科 4：硕士 5博士 6：其他", dataType="tinyint")
	private Byte education;

	/**
     * 身高
     * @definition decimal(10,2) unsigned DEFAULT '0.00' 
     */
    @ColumnProperty(name = "height", nullable = false, precision = 10, scale = 2, comment = "身高", dataType="decimal")
	private BigDecimal height;

	/**
     * 体重
     * @definition decimal(10,2) unsigned DEFAULT '0.00' 
     */
    @ColumnProperty(name = "weight", nullable = false, precision = 10, scale = 2, comment = "体重", dataType="decimal")
	private BigDecimal weight;

	/**
     * 兴趣爱好
     * @definition varchar(255) DEFAULT '' 
     */
    @ColumnProperty(name = "Interest", nullable = true, length = 255, comment = "兴趣爱好", dataType="varchar")
	private String interest;

	/**
     * 婚姻状态：0未婚，1已婚
     * @definition tinyint(1) unsigned DEFAULT '0' 
     */
    @ColumnProperty(name = "marital_status", nullable = false, precision = 3, comment = "婚姻状态：0未婚，1已婚", dataType="tinyint")
	private Byte maritalStatus;

	/**
     * 职业
     * @definition varchar(255) DEFAULT '' 
     */
    @ColumnProperty(name = "occupation", nullable = false, length = 255, comment = "职业", dataType="varchar")
	private String occupation;

	public UserInfo (){
		super();
	}
	
	public UserInfo (UserInfo that){
        super(that);
		this.userCode = that.userCode;
		this.userName = that.userName;
		this.userPasswd = that.userPasswd;
		this.weixinNo = that.weixinNo;
		this.mobile = that.mobile;
		this.qqNo = that.qqNo;
		this.telephone = that.telephone;
		this.email = that.email;
		this.sex = that.sex;
		this.age = that.age;
		this.province = that.province;
		this.city = that.city;
		this.address = that.address;
		this.remark = that.remark;
		this.education = that.education;
		this.height = that.height;
		this.weight = that.weight;
		this.interest = that.interest;
		this.maritalStatus = that.maritalStatus;
		this.occupation = that.occupation;
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return UserInfoDefinition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = UserInfoDefinition.values();
        return defs;
    }
}