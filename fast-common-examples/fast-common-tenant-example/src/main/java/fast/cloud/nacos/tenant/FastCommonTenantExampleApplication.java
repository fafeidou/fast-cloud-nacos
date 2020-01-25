package fast.cloud.nacos.tenant;

import fast.cloud.nacos.tenant.dynamic.mybatis.DynamicDataSourceAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {"fast.cloud.nacos.tenant", "fast.cloud.nacos.common.tenant"})
public class FastCommonTenantExampleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(FastCommonTenantExampleApplication.class, args);

        DynamicDataSourceAspect bean = applicationContext.getBean(DynamicDataSourceAspect.class);

        System.out.println(bean);
    }

}
