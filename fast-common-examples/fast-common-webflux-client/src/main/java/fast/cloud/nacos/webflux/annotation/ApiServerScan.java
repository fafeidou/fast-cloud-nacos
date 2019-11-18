package fast.cloud.nacos.webflux.annotation;

import fast.cloud.nacos.webflux.config.WebFluxRpcAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({WebFluxRpcAutoConfiguration.ExternalApiServerScannerRegistrar.class})
public @interface ApiServerScan {

    /**
     * `@ApiServer` 所注解的包扫描路径
     */
    String[] packages() default {};

}