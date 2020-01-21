package fast.cloud.nacos.common.tenant.config;

import fast.cloud.nacos.common.tenant.context.TenantStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import java.util.Arrays;

@Configuration
@EnableConfigurationProperties(TenantProperties.class)
public class TenantAutoConfig {

    private final TenantProperties tenantProperties;
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantAutoConfig.class);

    public TenantAutoConfig(TenantProperties tenantProperties) {
        this.tenantProperties = tenantProperties;
    }

    @Bean
    public Filter tenantFilter() {
        return new TenantFilter();
    }

    @Bean
    public FilterRegistrationBean tenantFilterRegistration() {
        FilterRegistrationBean result = new FilterRegistrationBean();
        result.setFilter(this.tenantFilter());
        result.setUrlPatterns(Arrays.asList("/*"));
        result.setName("Tenant Store Filter");
        result.setOrder(1);
        return result;
    }

    @PostConstruct
    public void afterPropertiesSet() {
        if (tenantProperties != null && !StringUtils.isEmpty(tenantProperties.getId())) {
            TenantStore.setApplicationTenantId(tenantProperties.getId());
            TenantStore.setApplicationTenant(true);
            LOGGER.debug("set Application TenantId {}", tenantProperties.getId());
        }
    }

}
