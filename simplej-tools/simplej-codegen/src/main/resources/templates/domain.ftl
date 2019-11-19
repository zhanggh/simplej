package ${package}.${business}.domain;
import com.haven.simplej.db.annotation.ColumnProperty;
import lombok.*;
import com.haven.simplej.db.annotation.*;
import java.sql.*;
import javax.persistence.*;
import java.math.BigDecimal;
import com.haven.simplej.db.base.BaseDomain;
import com.haven.simplej.db.base.EnumDomainDef;
import org.springframework.format.annotation.DateTimeFormat;
import org.apache.commons.lang3.StringUtils;
import ${package}.${business}.domain.definition.${entity.name}Definition;
/**
 * ${entity.comment}
 */
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@Table(name = "${entity.tableName}", schema = "${entity.schema}", indexes = { // catalog 定义为分表数<#list entity.indexes as idx><#if idx.indexName != 'PRIMARY'>
    @Index(name = "${idx.indexName}", columnList = "${idx.columnNameStr}", unique = ${(idx.nonUnique > 0)?string("false", "true")}), //</#if></#list>
})
public class ${entity.name} extends BaseDomain {
	private static final long serialVersionUID = 1L;
<#list entity.fields as fd><#if !baseFields?seq_contains(fd.name)>

	/**
     * ${fd.comment}
     * @definition ${fd.columnType} DEFAULT <#if fd.columnDefault??>'${fd.columnDefault}'<#else>NULL</#if> ${fd.extra}
     */<#if fd.type == "Time">
    @DateTimeFormat(pattern = "HH:mm:ss")</#if><#if fd.type == "Date">
    @DateTimeFormat(pattern = "yyyy-MM-dd")</#if><#if (fd.type == "java.util.Date" || fd.type == "Timestamp")>
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")</#if>
    @ColumnProperty(name = "${fd.columnName}", nullable = ${(fd.nullable = "YES")?string("true", "false")}<#if fd.characterLength??>, length = ${fd.characterLength?string("#")}</#if><#if fd.numericPrecision??>, precision = ${fd.numericPrecision}</#if><#if (fd.numericScale?? && fd.numericScale > 0)>, scale = ${fd.numericScale}</#if><#if fd.comment??>, comment = "${fd.comment}", dataType="${fd.dataType}"</#if>)
	private ${fd.type} ${fd.name};
</#if></#list>

	public ${entity.name} (){
		super();
	}
	
	public ${entity.name} (${entity.name} that){
        super(that);<#list entity.fields as fd><#if !baseFields?seq_contains(fd.name)>
		this.${fd.name} = that.${fd.name};</#if></#list>
	}

	@Override
	public String getTableName(){
		if (StringUtils.isNotEmpty(this.tableName)) {
			return this.tableName;
		}
        return ${entity.name}Definition.getTableName();
    }

	@Override
	public EnumDomainDef[] getFields(){
		EnumDomainDef[] defs = ${entity.name}Definition.values();
        return defs;
    }
}