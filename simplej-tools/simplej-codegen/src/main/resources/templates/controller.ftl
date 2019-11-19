package ${package}.${business}.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ${package}.${business}.model.${entity.name}Model;
import ${package}.${business}.service.${entity.name}Service;
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
    
    @Autowired
    private ${entity.name}Service service;

    /**
    * 查询特定id的${entity.name}记录信息
    */
    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public JsonResponse<${entity.name}Model> get(@PathVariable("id") long id){

        ${entity.name}Model model = new ${entity.name}Model();
        model.setId(id);
        return ResponseBuilder.build(this.service.get(model));
    }

    /**
    * 分页查询${entity.name}记录信息
    */
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public JsonResponse<PageInfo<${entity.name}Model>> search(${entity.name}Model model){

        return this.service.search(model);
    }

    /**
    * 软删除id的${entity.name}记录信息
    */
    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public JsonResponse<Integer> remove(${entity.name}Model model){

        return ResponseBuilder.build(this.service.remove(model) != 0);
    }

    /**
    * 新增${entity.name}记录信息
    */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public JsonResponse<Long> add(${entity.name}Model model){

        int resp = this.service.save(model);
        return ResponseBuilder.build(resp == 1);
    }

    /**
    * 更新特定id的${entity.name}记录信息
    */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public JsonResponse<Long> update(${entity.name}Model model){

        int resp = this.service.update(model);
        return ResponseBuilder.build(resp == 1);
    }
}