package fast.cloud.nacos.example.web.config;

import fast.cloud.nacos.example.web.service.HttpApiCollector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author qinfuxiang
 * @Date 2020/6/5 13:43
 */
@Configuration
public class HttpApiAutoConfiguration {

    @Bean
    @ConfigurationProperties(
        prefix = "ddmc.httpapi"
    )
    public HttpApiProperties httpApiProperties() {
        return new HttpApiProperties();
    }

    @Bean
    @ConditionalOnMissingBean({HttpApiCollector.class})
    public HttpApiCollector initPrivilegeCollector(HttpApiProperties httpApiProperties, RequestMappingHandlerMapping requestMappingHandlerMapping) {
        httpApiProperties.setInit(true);
        return new HttpApiCollector(httpApiProperties, requestMappingHandlerMapping);
    }
}