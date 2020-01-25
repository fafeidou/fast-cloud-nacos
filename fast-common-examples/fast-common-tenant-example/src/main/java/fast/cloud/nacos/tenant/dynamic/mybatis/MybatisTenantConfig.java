package fast.cloud.nacos.tenant.dynamic.mybatis;

import fast.cloud.nacos.common.tenant.mybatis.TenantInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MybatisTenantConfig {

    @Bean
    public TenantInterceptor tenantInterceptor() {
        return new TenantInterceptor();
    }
}
