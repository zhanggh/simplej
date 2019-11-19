package ${package}.${business}.rpc.service;

import ${package}.${business}.rpc.model.${entity.name}RpcModel;
import ${package}.${business}.model.${entity.name}Model;
import com.haven.simplej.bean.BeanUtil;
import com.haven.simplej.demo.service.${entity.name}Service;
import com.haven.simplej.response.model.JsonResponse;
import com.haven.simplej.response.model.PageInfo;
import com.haven.simplej.rpc.annotation.RpcStruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.vip.vjtools.vjkit.mapper.BeanMapper;

import java.util.List;

/**
 * 远程服务实现类
 * ${entity.comment}RpcServiceImpl
 */
@Service
@Slf4j
public class ${entity.name}RpcServiceImpl implements ${entity.name}RpcService {

    @Autowired
    private ${entity.name}Service service;

    /**
     *查询${entity.name}信息接口服务
     */
    public ${entity.name}RpcModel get(@RpcStruct ${entity.name}RpcModel model){
    <#if useDbFlag == 1>
        ${entity.name}Model dbModel = BeanMapper.map(model,${entity.name}Model.class);
        dbModel = service.get(dbModel);
        BeanUtil.copy(dbModel,model);
    </#if>
        return model;
    }

    /**
    *新增${entity.name}信息接口服务
    */
    public int add(@RpcStruct ${entity.name}RpcModel model){
        int resp;
    <#if useDbFlag == 1>
        ${entity.name}Model dbModel = BeanMapper.map(model,${entity.name}Model.class);
        resp = service.create(dbModel);
    </#if>
        return resp;
    }

    /**
    *修改${entity.name}信息接口服务, 根据model的id进行数据修改
    */
    public int update(@RpcStruct ${entity.name}RpcModel model){
        int resp;
        <#if useDbFlag == 1>
        ${entity.name}Model dbModel = BeanMapper.map(model,${entity.name}Model.class);
        resp = service.update(dbModel);
        </#if>
        return resp;
    }

    /**
    * 统计${entity.name}笔数接口服务
    */
    public int count(@RpcStruct ${entity.name}RpcModel model){
        int resp;
        <#if useDbFlag == 1>
        ${entity.name}Model dbModel = BeanMapper.map(model,${entity.name}Model.class);
        resp = service.count(dbModel);
        </#if>
        return resp;
    }

    /**
    *批查询${entity.name}信息接口服务
    */
    public List<${entity.name}RpcModel> query(@RpcStruct ${entity.name}RpcModel model){
        List<${entity.name}RpcModel> resp;
    <#if useDbFlag == 1>
        ${entity.name}Model dbModel = BeanMapper.map(model,${entity.name}Model.class);
        List<${entity.name}Model> list = service.query(dbModel);
        resp = BeanMapper.mapList(list,${entity.name}RpcModel.class);
    </#if>
        return resp;
    }

    /**
    *批分页查询${entity.name}信息接口服务
    */
    public JsonResponse<PageInfo<${entity.name}RpcModel>> search(${entity.name}RpcModel model){
        JsonResponse<PageInfo<${entity.name}RpcModel>> jsonResponse = null;
        <#if useDbFlag == 1>
             ${entity.name}Model dbModel = BeanMapper.map(model,${entity.name}Model.class);
             JsonResponse<PageInfo<${entity.name}Model>> resp = this.service.search(dbModel);
             jsonResponse = BeanMapper.map(resp,JsonResponse.class);
        </#if>
        return jsonResponse;
    }

    /**
    *软删除${entity.name}信息接口服务
    */
    public int delete(@RpcStruct ${entity.name}RpcModel model){
        int resp;
        <#if useDbFlag == 1>
            ${entity.name}Model dbModel = BeanMapper.map(model,${entity.name}Model.class);
            resp = service.update(dbModel);
        </#if>
        return resp;
    }

    /**
    *${entity.name}接口服务健康度监测方法，服务治理中心定期向该接口方法发起检测，
    * 请自定义用于判断该service服务正常状态的逻辑，正常返回0.否则返回其他非0数值
    */
    public int healthCheck(){
        //@TODO
    <#if useDbFlag == 1>
        //只是一个例子
        ${entity.name}Model dbModel = new ${entity.name}Model();
        dbModel.setId(1L);
        dbModel = service.get(dbModel);
    </#if>
        return 0;
    }

}