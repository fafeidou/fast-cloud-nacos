package fast.cloud.nacos.tenant;

import fast.cloud.nacos.tenant.dynamic.mybatis.DynamicDataSourceAspect;
import fast.cloud.nacos.tenant.entity.DemoEntity;
import fast.cloud.nacos.tenant.event.DemoEvent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {"fast.cloud.nacos.tenant", "fast.cloud.nacos.common.tenant","fast.cloud.nacos.common.model"})
public class FastCommonTenantExampleApplication /*extends SpringBootServletInitializer*/ implements CommandLineRunner {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(FastCommonTenantExampleApplication.class, args);

        DynamicDataSourceAspect bean = applicationContext.getBean(DynamicDataSourceAspect.class);

        System.out.println(bean);

    }

    @Override
    public void run(String... args) throws Exception {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setName("name");
        demoEntity.setId(11l);
        DemoEvent demoEvent = new DemoEvent(demoEntity);
        demoEvent.publish();
    }

    /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(FastCommonTenantExampleApplication.class);
    }*/
}
