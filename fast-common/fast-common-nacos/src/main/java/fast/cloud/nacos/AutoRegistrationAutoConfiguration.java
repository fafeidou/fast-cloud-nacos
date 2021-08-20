package fast.cloud.nacos;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import fast.cloud.nacos.warmup.WarmUpComponent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
//@Import({AutoServiceRegistrationAutoConfiguration.class})
@ConditionalOnProperty(
    value = {"spring.cloud.service-registry.auto-registration.enabled"},
    havingValue = "false",
    matchIfMissing = false
)
@ConditionalOnNacosDiscoveryEnabled
@EnableConfigurationProperties(AutoServiceRegistrationProperties.class)

//@AutoConfigureAfter({ AutoServiceRegistrationConfiguration.class,
//        AutoServiceRegistrationAutoConfiguration.class })
public class AutoRegistrationAutoConfiguration {

    @Bean
    public DelayedNacosAutoRegistration delayedNacosAutoRegistration(ServiceRegistry<Registration> serviceRegistry,
                                                                     AutoServiceRegistrationProperties autoServiceRegistrationProperties,
                                                                     NacosRegistration registration) {
        return new DelayedNacosAutoRegistration(serviceRegistry, autoServiceRegistrationProperties, registration);
    }

    @Bean
    public AutoRegistration autoRegistration(List<WarmUpComponent> warmUpComponents,
                                             DelayedNacosAutoRegistration delayedZkAutoRegistration) {
        return new AutoRegistration(warmUpComponents, delayedZkAutoRegistration);
    }

    @Bean
    public NacosServiceRegistry nacosServiceRegistry(
            NacosDiscoveryProperties nacosDiscoveryProperties) {
        return new NacosServiceRegistry(nacosDiscoveryProperties);
    }

    @Bean
    public NacosRegistration nacosRegistration(
            NacosDiscoveryProperties nacosDiscoveryProperties,
            ApplicationContext context) {
        return new NacosRegistration(nacosDiscoveryProperties, context);
    }
}
