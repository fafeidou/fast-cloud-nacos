package fast.cloud.nacos.tenant.dynamic.mybatis;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import fast.cloud.nacos.tenant.enums.DS;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AutoConfigureBefore(value = {DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
@MapperScan(basePackages = {"fast.cloud.nacos.tenant.mapper"})
public class MybatisDataSourceConfig {

    @Bean(DS.MY_CAT)
    @ConfigurationProperties(prefix = "spring.datasource.druid.my-cat")
    public DataSource myCatDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(DS.TENANT_MANAGE)
    @ConfigurationProperties(prefix = "spring.datasource.druid.tenant-manager")
    public DataSource tenantManager() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean("dynamicDataSource")
    @Primary
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DS.MY_CAT, myCatDataSource());
        dataSourceMap.put(DS.TENANT_MANAGE, tenantManager());
        // 将 master 数据源作为默认指定的数据源
        dynamicDataSource.setDefaultDataSource(myCatDataSource());
        // 将 master 和 slave 数据源作为指定的数据源
        dynamicDataSource.setDataSources(dataSourceMap);
        return dynamicDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        // 配置事务管理, 使用事务时在方法头部添加@Transactional注解即可
        return new DataSourceTransactionManager(dynamicDataSource());
    }

}
