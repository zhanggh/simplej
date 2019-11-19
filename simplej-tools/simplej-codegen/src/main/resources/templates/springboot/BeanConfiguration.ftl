package ${package}.${business}.config;
<#if useDbFlag == 1>
import com.haven.simplej.db.configuration.BaseDataSourceConfiguration;
</#if>
import com.haven.simplej.property.PropertyManager;
import com.haven.simplej.spring.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
<#if useRpcFlag == 1>
import ${package}.${business}.StartUpApplication;
import com.haven.simplej.rpc.server.server.RpcServerContext;
import com.haven.simplej.rpc.filter.RpcFilter;
import com.haven.simplej.rpc.filter.impl.RpcLogFilter;
import java.util.List;
</#if>

/**
 * Application Configuration Adapter<br>
 * @author haven.zhang
 */
@Configuration
<#if useRpcFlag == 1 && !forTest>
@Import(RpcConfiguration.class)
</#if>
@Slf4j
public class BeanConfiguration <#if useRpcFlag == 1 && moduleType == "WEB"><#else><#if useDbFlag == 1>extends BaseDataSourceConfiguration</#if></#if> {



    public BeanConfiguration() {
        log.info("init BeanConfiguration");
    }

<#if useRpcFlag != 1>
    @Bean
    @Order(1)
    @ConditionalOnMissingBean(SpringContext.class)
    public SpringContext springContext() {
        log.info("init springcontext ");

        return new SpringContext();
    }

    /**
     * 实例化属性管理器
     */
    @Bean
    public PropertyManager propertyManager() {
        log.info("init propertyManager");

        return new PropertyManager();
    }
</#if>
    <#if useRpcFlag == 1 && moduleType == "WEB"><#else>
    <#if useDbFlag == 1>

    /*
     *分表分库的时候需要实现该方法，单库场景下，可以忽略
     */
    @Override
    public String getShardingAopExpress() {
        //此处表达式匹配使用了分表分库组件（@see @RepositorySharding）的包&类、方法
        //在多个表达式之间使用  || , or 表示  或 ，使用  && , and 表示  与 ， ！ 表示非
        return "execution(* ${package}.${business}.service.*.*(..)) || execution(* ${package}.${business}.service2.*.*(..))";
    }
    </#if>
    </#if>
}


