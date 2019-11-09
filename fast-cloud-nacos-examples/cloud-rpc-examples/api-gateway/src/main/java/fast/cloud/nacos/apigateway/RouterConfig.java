package fast.cloud.nacos.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Batman.qin
 * @create 2019-01-14 10:08
 */
@Configuration
public class RouterConfig {
    static final String prefix = "/admin";

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("SERVICE-FEIGN", r ->
                        r.path(prefix + "/service-hi/**").
                                filters(f -> f.rewritePath(prefix + "/service-hi/(?<remaining>.*)", "/$\\{remaining}"))
                                .uri("lb://service-hi"))
                .build();

    }

}
