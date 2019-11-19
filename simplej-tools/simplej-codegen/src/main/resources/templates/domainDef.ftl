package ${package}.${business}.domain.definition;
import com.haven.simplej.db.annotation.ColumnProperty;
import com.haven.simplej.db.base.EnumDomainDef;
import com.haven.simplej.db.util.DomainUtil;
import ${package}.${business}.domain.${entity.name};


/**
 * ${entity.comment} 字段属性枚举类
 */
public enum ${entity.name}Definition implements EnumDomainDef {

<#list entity.fields as fd>
	${fd.name}<#if fd_has_next>,</#if><#if !fd_has_next>;</#if>
</#list>

    /**
     * 获取对应字段的属性信息
	 */
	private final ColumnProperty propertyModel = DomainUtil.getPropertyModel(${entity.name}.class, this.name());

	public ColumnProperty getPropertyModel() {
		return this.propertyModel;
	}

	public static boolean isExistPropertyName(String propertyName) {
		return DomainUtil.isExistValue(values(), EnumDomainDef::name, propertyName);
	}

	public static String getTableName() {
		return "${entity.schema}.${entity.tableName}";
	}
}