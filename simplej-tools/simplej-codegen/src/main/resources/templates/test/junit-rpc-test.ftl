package ${package}.${business}.rpc.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import ${package}.${business}.rpc.model.${entity.name}RpcModel;
import com.haven.simplej.rpc.client.client.proxy.ServiceProxy;
import com.haven.simplej.rpc.constant.RpcConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * ${entity.name}RpcService 远程调用单元测试类
 * @author haven.zhang
 * @date 2018/12/27.
 */
public class ${entity.name}RpcServiceTest {

    /**
     * rpc客户端
     */
    private ${entity.name}RpcService service = ServiceProxy.create().setInterfaceClass(${entity.name}RpcService.class).build();

    static {
        //目标服务ip，如果指定了该ip地址，则请求将直接到达目标服务，不会经过proxy转发
        System.setProperty(RpcConstants.RPC_APP_SERVER_HOST_KEY, "127.0.0.1");
        //目标服务端口
        System.setProperty(RpcConstants.RPC_APP_SERVER_PORT_KEY, "8899");

        //proxy服务的ip，一般proxy都会部署在每个业务应用服务的当前节点上均有部署
        //		System.setProperty(RpcConstants.RPC_PROXY_HOST_KEY,"127.0.0.1");
        //proxy服务的端口
        //		System.setProperty(RpcConstants.RPC_PROXY_PORT_KEY,"8086");
    }

    @Test
    public void getTest(){
        ${entity.name}RpcModel model = new ${entity.name}RpcModel();
        model.setId(1L);
        System.out.println(JSON.toJSONString(service.get(model),true));
    }

    @Test
    public void count(){
        ${entity.name}RpcModel model = new ${entity.name}RpcModel();
        model.setId(1L);

        System.out.println(JSON.toJSONString(service.count(model), true));
    }

    @Test
    public void query(){
        ${entity.name}RpcModel model = new ${entity.name}RpcModel();
        model.setId(1L);

        System.out.println(JSON.toJSONString(service.query(model), true));
    }

    @Test
    public void  search(){
        ${entity.name}RpcModel model = new ${entity.name}RpcModel();
        model.setId(1L);

        System.out.println(JSON.toJSONString(service.search(model), true));
    }

    @Test
    public void create(){
        ${entity.name}RpcModel model = new ${entity.name}RpcModel();
        model.setId(1L);
        model.setCreateTime(new Timestamp(new Date().getTime()));
        model.setCreatedBy("test");
        model.setUpdateTime(new Timestamp(new Date().getTime()));
        model.setUpdatedBy("test");
        model.setIsDeleted((byte) 0);
        System.out.println(JSON.toJSONString(service.add(model), true));
    }

    @Test
    public void  update(){
        ${entity.name}RpcModel model = new ${entity.name}RpcModel();
        model.setId(1L);
        model.setCreateTime(new Timestamp(new Date().getTime()));
        model.setCreatedBy("test");
        model.setUpdateTime(new Timestamp(new Date().getTime()));
        model.setUpdatedBy("test");
        model.setIsDeleted((byte) 0);
        System.out.println(JSON.toJSONString(service.update(model), true));
    }

    @Test
    public void  remove(){
        ${entity.name}RpcModel model = new ${entity.name}RpcModel();
        model.setId(1L);
        System.out.println(JSON.toJSONString(service.delete(model), true));
    }
}
