package fast.cloud.nacos.cat.monitor.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatFilterConfigure {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static volatile boolean analysisEnable;

    @Bean
    public FilterRegistrationBean catFilter() {
        FilterRegistrationBean<CatContextServletFilter> registration = new FilterRegistrationBean();
        CatContextServletFilter filter = new CatContextServletFilter(mapper);
        registration.setFilter(filter);
        registration.addUrlPatterns(new String[]{"/*"});
        registration.setName("cat-filter");
        registration.setOrder(1);
        return registration;
    }
}

