package fast.cloud.nacos.example.web.swagger;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author batman.qin
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(value = SwaggerConfig.class)
@EnableAutoConfiguration
@ConditionalOnBean(SwaggerConfig.class)
@EnableSwaggerBootstrapUI
public class CkSwaggerAutoConfiguration implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(CkSwaggerAutoConfiguration.class);
    private final SwaggerConfig swaggerConfig;
    private ConfigurableApplicationContext configurableApplicationContext;

    public CkSwaggerAutoConfiguration(SwaggerConfig swaggerConfig) {
        this.swaggerConfig = swaggerConfig;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Bean
    public String createDocket() {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Docket.class);

        beanDefinitionBuilder.addConstructorArgValue(DocumentationType.SWAGGER_2);

        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();

        if (!CollectionUtils.isEmpty(swaggerConfig.getModules())) {

            swaggerConfig.getModules().forEach(module -> {
                beanFactory.registerBeanDefinition(module.getGroupName(), beanDefinition);

                Docket docket = configurableApplicationContext.getBean(module.getGroupName(), Docket.class);

                List<Predicate<RequestHandler>> list = Lists.newArrayList();
                if (!CollectionUtils.isEmpty(module.getPackages())) {
                    module.getPackages().forEach(str -> list.add(RequestHandlerSelectors.basePackage(str)));
                }
//                ParameterBuilder tokenPar = new ParameterBuilder();
//                ParameterBuilder Username = new ParameterBuilder();
//                List<Parameter> pars = new ArrayList<Parameter>();
//                tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
//                Username.name("username").description("用户名").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
//                pars.add(tokenPar.build());
//                pars.add(Username.build());
                docket/*.globalOperationParameters(pars)*/.apiInfo(apiInfo())
                        .groupName(Optional.ofNullable(module.getGroupName()).orElse(""))
                        .pathMapping("")
                        .select()
                        .apis(Predicates.or(list)::apply)
                        .paths(PathSelectors.any())
                        .build();
            });

        }
        return "createDocket";

    }

    /**
     * 配置认证模式
     */
    private List<ApiKey> securitySchemes() {
        return newArrayList(new ApiKey("Authorization", "Authorization", "header"));
    }

    /**
     * 配置认证上下文
     */
    private List<SecurityContext> securityContexts() {
        return newArrayList(SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build());
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(new SecurityReference("Authorization", authorizationScopes));
    }

    /**
     * 项目信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(StringUtils.isEmpty(swaggerConfig.getTitle()) ? "CHARLES-KEITH RESTful APIs" : swaggerConfig.getTitle())
                .description(StringUtils.isEmpty(swaggerConfig.getDescription()) ? "桦洁商贸电子商务微服务接口文档" : swaggerConfig.getDescription())
                .license("")
                .version(StringUtils.isEmpty(swaggerConfig.getVersion()) ? "1.0" : swaggerConfig.getVersion())
                .build();
    }


}
