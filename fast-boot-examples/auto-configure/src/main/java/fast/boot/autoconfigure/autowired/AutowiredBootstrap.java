package fast.boot.autoconfigure.autowired;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AutowiredBootstrap {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AutowiredBootstrap.class, args);
        A a = context.getBean(A.class);
        System.out.println(a);
    }
}
