package ${package}.${business}.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import ${package}.${business}.model.${entity.name}Model;
<#if useDbFlag == 1>
<#if datasourceType == "sharding">import com.haven.simplej.db.annotation.DataSource;</#if>
<#if daoType == "mybatis">
import com.haven.simplej.db.base.BaseMapper;
import com.haven.simplej.db.base.BaseServiceImpl;
import ${package}.${business}.mapper.${entity.name}Mapper;
</#if>
import ${package}.${business}.service.${entity.name}Service;
<#if daoType != "mybatis">
import com.haven.simplej.db.base.BaseServiceImpl2;
</#if>
</#if>
/**
 * ${entity.comment} Service implements
 */
@Slf4j
@Service
<#if datasourceType == "sharding">@DDataSource(dbName = "${entity.schema}")</#if>
public class ${entity.name}ServiceImpl <#if useDbFlag == 1>extends <#if daoType !=
"mybatis">BaseServiceImpl2<${entity.name}Model> </#if><#if daoType == "mybatis">BaseServiceImpl<${entity.name}Model>
        </#if></#if>implements ${entity.name}Service {

<#if daoType == "mybatis">
    @Autowired
	private ${entity.name}Mapper mapper;

    @Override
    protected BaseMapper<${entity.name}Model> getBaseMapper(){
        return mapper;
    }
</#if>
<#if useDbFlag == 0>
    @Override
    public ${entity.name}Model get(${entity.name}Model model){
        model = new ${entity.name}Model();
        model.setId(1L);
        return model;
    }

    @Override
    public int remove(${entity.name}Model model){
        //@TODO do something here
        return 0;
    }

    @Override
    public int save(${entity.name}Model model){

        //@TODO do something here
        return 0;
    }

    @Override
    public int update(${entity.name}Model model){

        //@TODO do something here
        return 0;
    }

    @Override
    public JsonResponse<PageInfo<${entity.name}Model>> search(${entity.name}Model model){
        JsonResponse<PageInfo<${entity.name}Model>>  jsonResponse = new JsonResponse<>();
        //@TODO do something here
        return jsonResponse;
    }
</#if>
}