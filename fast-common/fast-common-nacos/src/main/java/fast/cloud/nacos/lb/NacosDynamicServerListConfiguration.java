package fast.cloud.nacos.lb;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.ConditionalOnRibbonNacos;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnRibbonNacos
@ConditionalOnNacosDiscoveryEnabled
public class NacosDynamicServerListConfiguration {
    @Bean
    public NacosDynamicServerListUpdater dynamicServerListUpdater(NacosDiscoveryProperties properties) throws NacosException {
        return new NacosDynamicServerListUpdater(NacosFactory.createNamingService(properties.getServerAddr()), properties);
    }

}
