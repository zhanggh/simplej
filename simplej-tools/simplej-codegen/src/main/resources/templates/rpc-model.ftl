package ${package}.${business}.rpc.model;

import lombok.*;
import com.haven.simplej.rpc.annotation.RpcParam;
import com.haven.simplej.rpc.annotation.RpcStruct;
import com.haven.simplej.rpc.annotation.Doc;
import java.sql.Timestamp;

/**
 * rpc 接口请求model
 * ${entity.comment}
 */
@Getter
@Setter
@ToString(callSuper = true)
@RpcStruct
public class ${entity.name}RpcModel {
	private static final long serialVersionUID = 1L;

<#list entity.fields as fd>

    /**
    * ${fd.comment}
    * @definition ${fd.columnType} DEFAULT <#if fd.columnDefault??>'${fd.columnDefault}'<#else>NULL</#if> ${fd.extra}
    */
    @Doc("${fd.comment}")
    @RpcParam(required="false"<#if fd.characterLength??>,maxLength=${fd
.characterLength?string("#")}</#if><#if fd.numericPrecision??>,maxLength=${fd.numericPrecision}</#if>)
    private ${fd.type} ${fd.name};
</#list>
}