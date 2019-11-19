package ${package}.${business}.rpc.service;

import ${package}.${business}.rpc.model.${entity.name}RpcModel;
import com.haven.simplej.response.model.JsonResponse;
import com.haven.simplej.response.model.PageInfo;
import com.haven.simplej.rpc.annotation.*;
import java.util.List;
/**
 * 远程服务接口api
 * 原则上，当接口的参数由变更，方法有变更，都应该要升级版本号
 * ${entity.comment}RpcService
 */
@RpcService(version="1.0")
public interface ${entity.name}RpcService {

    @RpcMethod(timeout = 100)
    @Doc("查询${entity.name}信息接口服务")
    ${entity.name}RpcModel get(@RpcStruct ${entity.name}RpcModel model);

    @RpcMethod(timeout = 100)
    @Doc("新增${entity.name}信息接口服务")
    int add(@RpcStruct ${entity.name}RpcModel model);

    @RpcMethod(timeout = 100)
    @Doc("修改${entity.name}信息接口服务")
    int update(@RpcStruct ${entity.name}RpcModel model);

    @RpcMethod(timeout = 100)
    @Doc("统计${entity.name}笔数接口服务")
    int count(@RpcStruct ${entity.name}RpcModel model);

    @RpcMethod(timeout = 100)
    @Doc("软删除${entity.name}信息接口服务")
    int delete(@RpcStruct ${entity.name}RpcModel model);

    @RpcMethod(timeout = 200)
    @Doc("批查询${entity.name}信息接口服务")
    List<${entity.name}RpcModel> query(@RpcStruct ${entity.name}RpcModel model);

    @RpcMethod(timeout = 200)
    @Doc("批分页查询${entity.name}信息接口服务")
    JsonResponse<PageInfo<${entity.name}RpcModel>> search(${entity.name}RpcModel model);

    @HealthCheck(timeout = 10)
    @Doc("${entity.name}接口服务健康度监测方法，服务治理中心定期向该接口方法发起检测，请自定义用于判断该service服务正常状态的逻辑，正常返回0.否则返回其他非0数值")
    int healthCheck();


}