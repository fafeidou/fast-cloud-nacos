package fast.cloud.nacos.orderservice.config;

/**
 * @Classname GlobalTransactionAutoConfiguration
 * @Description TODO
 * @Date 2020/4/6 10:48
 * @Created by qinfuxiang
 */

import io.seata.spring.annotation.GlobalTransactionScanner;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author IT云清
 */
@Configuration
@EnableConfigurationProperties({SeataProperties.class})
public class GlobalTransactionAutoConfiguration {
    private static final String APPLICATION_NAME_PREFIX = "spring.application.name";
    private static final String DEFAULT_TX_SERVICE_GROUP_SUFFIX = "-seata-service-group";
    private final ApplicationContext applicationContext;
    private final SeataProperties seataProperties;

    public GlobalTransactionAutoConfiguration(
            ApplicationContext applicationContext,
            SeataProperties seataProperties) {
        this.applicationContext = applicationContext;
        this.seataProperties = seataProperties;
    }

    /**
     * If there is no txServiceGroup,use the default
     *
     * @return GlobalTransactionScanner the entrance
     */
    @Bean
    public GlobalTransactionScanner globalTransactionScanner() {
        String applicationName = this.applicationContext.getEnvironment().getProperty(APPLICATION_NAME_PREFIX);
        String txServiceGroup = seataProperties.getTxServiceGroup();
        if (StringUtils.isEmpty(txServiceGroup)) {
            txServiceGroup = applicationName + DEFAULT_TX_SERVICE_GROUP_SUFFIX;
            this.seataProperties.setTxServiceGroup(txServiceGroup);
        }
        return new GlobalTransactionScanner(applicationName, txServiceGroup);
    }
}


