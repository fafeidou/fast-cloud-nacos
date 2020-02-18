package fast.cloud.nacos.common.model.log;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnClass(HandlerInterceptor.class)
@ConditionalOnWebApplication
public class InvokeAutoConfiguration implements WebMvcConfigurer {

    //    @Bean
//    public InvokeInterceptor invokeInterceptor() {
//        return new InvokeInterceptor();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(invokeInterceptor())
//                .addPathPatterns("/**");
//
//    }

    @Bean
    public RequestFilter userRequestFilter() {
        return new RequestFilter();
    }
}
