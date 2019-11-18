package fast.cloud.nacos.consumer;

import fast.cloud.nacos.consumer.service.IUserApi;
import fast.cloud.nacos.webflux.annotation.ApiServerScan;
import fast.cloud.nacos.webflux.interfaces.ProxyCreator;
import fast.cloud.nacos.webflux.proxys.JDKProxyCreator;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "fast.cloud.nacos")
@ApiServerScan(packages = "fast.cloud.nacos.consumer.service")
public class SpringWebfluxClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringWebfluxClientApplication.class, args);

    }

    /**
     * 创建jdk代理工具类
     *
     * @return
     */
   /* @Bean
    ProxyCreator jdkProxyCreator() {
        return new JDKProxyCreator();
    }

    @Bean
    FactoryBean<IUserApi> userApi(ProxyCreator proxyCreator) {
        return new FactoryBean<IUserApi>() {

            @Override
            public Class<?> getObjectType() {
                return IUserApi.class;
            }


            @Override
            public IUserApi getObject() {
                return (IUserApi) proxyCreator
                        .createProxy(this.getObjectType());
            }
        };
    }*/
}
