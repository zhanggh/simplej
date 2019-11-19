package ${package}.${business}.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import ${package}.${business}.StartUpApplication;
import ${package}.${business}.model.${entity.name}Model;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * ${entity.name}Service单元测试类
 * @author haven.zhang
 * @date 2018/12/27.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartUpApplication.class)
public class ${entity.name}ServiceTest {

    @Autowired
    private ${entity.name}Service service;


    @Test
    public void getTest(){
        ${entity.name}Model model = new ${entity.name}Model();
        model.setId(1L);
        System.out.println(JSON.toJSONString(service.get(model),true));
    }

    @Test
    public void count(){
        ${entity.name}Model model = new ${entity.name}Model();
        model.setId(1L);

        System.out.println(JSON.toJSONString(service.count(model), true));
    }

    @Test
    public void query(){
        ${entity.name}Model model = new ${entity.name}Model();
        model.setId(1L);

        System.out.println(JSON.toJSONString(service.query(model), true));
    }

    @Test
    public void  search(){
        ${entity.name}Model model = new ${entity.name}Model();
        model.setId(1L);

        System.out.println(JSON.toJSONString(service.search(model), true));
    }

    @Test
    public void create(){
        ${entity.name}Model model = new ${entity.name}Model();
        model.setId(1L);
        model.setCreateTime(new Timestamp(new Date().getTime()));
        model.setCreatedBy("test");
        model.setUpdateTime(new Timestamp(new Date().getTime()));
        model.setUpdatedBy("test");
        model.setIsDeleted((byte) 0);
        System.out.println(JSON.toJSONString(service.create(model), true));
    }

    @Test
    public void batchInsert(){
        List<${entity.name}Model> list = Lists.newArrayList();
        ${entity.name}Model model = new ${entity.name}Model();
        model.setId(1L);
        model.setCreateTime(new Timestamp(new Date().getTime()));
        model.setCreatedBy("test");
        model.setUpdateTime(new Timestamp(new Date().getTime()));
        model.setUpdatedBy("test");
        model.setIsDeleted((byte) 0);
        list.add(model);
        System.out.println(JSON.toJSONString(service.batchInsert(list), true));
    }

    @Test
    public void  update(){
        ${entity.name}Model model = new ${entity.name}Model();
        model.setId(1L);
        model.setCreateTime(new Timestamp(new Date().getTime()));
        model.setCreatedBy("test");
        model.setUpdateTime(new Timestamp(new Date().getTime()));
        model.setUpdatedBy("test");
        model.setIsDeleted((byte) 0);
        System.out.println(JSON.toJSONString(service.update(model), true));
    }

    @Test
    public void  batchUpdate(){
        List<${entity.name}Model> list = Lists.newArrayList();
        ${entity.name}Model model = new ${entity.name}Model();
        model.setId(1L);
        model.setCreateTime(new Timestamp(new Date().getTime()));
        model.setCreatedBy("test");
        model.setUpdateTime(new Timestamp(new Date().getTime()));
        model.setUpdatedBy("test");
        model.setIsDeleted((byte) 0);
        list.add(model);
        System.out.println(JSON.toJSONString(service.batchUpdate(list, new String[]{"id"}), true));
    }

    @Test
    public void  remove(){
        ${entity.name}Model model = new ${entity.name}Model();
        model.setId(1L);
        System.out.println(JSON.toJSONString(service.remove(model), true));
    }
}
