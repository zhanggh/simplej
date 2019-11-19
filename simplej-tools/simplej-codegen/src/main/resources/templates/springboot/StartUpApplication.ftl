package ${package}.${business};

<#--import ${package}.${business}.config.BeanConfiguration;-->
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.ImportResource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author haven.zhang
 * @date 2018/11/28.
 */
@Slf4j
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@PropertySource(value = {"classpath:/application.properties"<#if useRpcFlag == 1>,"classpath:/rpc-client.properties",
    "classpath:/rpc-server.properties"</#if>})
<#if useRpcFlag == 1 && moduleType == "WEB"><#else>
<#if daoType == "mybatis">@ImportResource(locations={"classpath:/mybatis.xml"})
</#if></#if>
public class StartUpApplication {

    public static void main(String[] args) {
        //启动服务
        String logHome = "logs";
        System.setProperty("spring.pid.file", logHome + File.separator + "application.pid");
        SpringApplication application = new SpringApplication(StartUpApplication.class);
        ApplicationPidFileWriter pidFileWriter = new ApplicationPidFileWriter();
        pidFileWriter.setTriggerEventType(ApplicationReadyEvent.class);
        application.addListeners(pidFileWriter);
        application.addListeners((ApplicationListener<ApplicationFailedEvent>) event -> {
            log.error("spring init error exit process", event.getException());
            System.exit(-2);
        });
        application.run(args);
    }
}
