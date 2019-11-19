package ${package}.${business}.config;

import com.haven.simplej.web.config.WebConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author haven.zhang
 * @date 2019/1/28.
 */
@Slf4j
@Configuration
public class WebInitConfiguration extends WebConfiguration {

	public WebInitConfiguration(){
		log.debug("init WebInitConfiguration");
	}
}
