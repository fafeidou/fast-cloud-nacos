package fast.cloud.nacos.tenant;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = {"fast.cloud.nacos.tenant","fast.cloud.nacos.common.tenant"})
public class FastCommonTenantExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastCommonTenantExampleApplication.class, args);
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.my-cat")
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }
}
