package ${package}.${business}.web.controller;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ${package}.${business}.rpc.model.${entity.name}RpcModel;
import ${package}.${business}.rpc.service.${entity.name}RpcService;
import com.haven.simplej.response.model.*;
import com.haven.simplej.response.builder.ResponseBuilder;

/**
 * ${entity.comment} 控制类
 * ${entity.comment}Controller
 */
@Slf4j
@RestController
@RequestMapping("/${mapping}/${entity.name?lower_case}")
public class ${entity.name}Controller {
    
    /**
    * rpc 客户端实例化
    */
    private ${entity.name}RpcService service = ServiceProxy.create().setInterfaceClass(${entity.name}RpcService.class).build();

    /**
    * 查询特定id的${entity.name}记录信息
    */
    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public JsonResponse<${entity.name}RpcModel> get(@PathVariable("id") long id){

        ${entity.name}RpcModel model = new ${entity.name}RpcModel();
        model.setId(id);
        return ResponseBuilder.build(this.service.get(model));
    }

    /**
    * 分页查询${entity.name}记录信息
    */
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public JsonResponse<PageInfo<${entity.name}RpcModel>> search(${entity.name}RpcModel model){

        return this.service.search(model);
    }

    /**
    * 软删除id的${entity.name}记录信息
    */
    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public JsonResponse<Integer> remove(${entity.name}RpcModel model){

        return ResponseBuilder.build(this.service.delete(model) != 0);
    }

    /**
    * 新增${entity.name}记录信息
    */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public JsonResponse<Long> add(${entity.name}RpcModel model){

        int resp = this.service.add(model);
        return ResponseBuilder.build(resp == 1);
    }

    /**
    * 更新特定id的${entity.name}记录信息
    */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public JsonResponse<Long> update(${entity.name}RpcModel model){

        int resp = this.service.update(model);
        return ResponseBuilder.build(resp == 1);
    }

    /**
    * controller 服务健康度监测方法，暴露该方法，服务治理中心会定期进行健康检查
    */
    @RequestMapping(value = "healthCheck")
    public JsonResponse<String> healthCheck(){
        return ResponseBuilder.build(true);
    }
}