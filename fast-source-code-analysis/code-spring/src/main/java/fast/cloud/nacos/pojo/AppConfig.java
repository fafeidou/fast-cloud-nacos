package fast.cloud.nacos.pojo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(lazyInit = true)
public class AppConfig {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        BizPerson bean = annotationConfigApplicationContext.getBean(BizPerson.class);
        bean.service();
        annotationConfigApplicationContext.registerShutdownHook();
    }
}
