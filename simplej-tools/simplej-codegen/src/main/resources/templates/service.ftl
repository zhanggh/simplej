package ${package}.${business}.service;

import com.haven.simplej.db.base.BaseService;
import ${package}.${business}.model.${entity.name}Model;


/**
 * ${entity.comment} Service
 */
public interface ${entity.name}Service<#if useDbFlag == 1> extends BaseService<${entity.name}Model> </#if>{

    <#if useDbFlag == 0>
    /**
     * 查询${entity.name}Model 信息
     */
    ${entity.name}Model get(${entity.name}Model model);

    /**
     * 软删除${entity.name}Model 信息
     */
    int remove(${entity.name}Model model);

    /**
     * 新增${entity.name}Model 信息
     */
    int save(${entity.name}Model model);

    /**
     * 更新${entity.name}Model 信息
     */
    int update(${entity.name}Model model);

    /**
     * 分页查询${entity.name}Model 信息
     */
    JsonResponse<PageInfo<${entity.name}Model>> search(${entity.name}Model model);

    </#if>
}