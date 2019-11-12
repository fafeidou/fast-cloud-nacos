package fast.boot.autoconfigure.bootstrap;

import fast.boot.autoconfigure.annotation.EnableHelloWorld;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

@EnableHelloWorld
public class EnableHelloWorldBootstrap {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(EnableHelloWorldBootstrap.class, args);
        String helloWorld = context.getBean("helloWorld", String.class);
        System.out.println("hello world bean:" + helloWorld);
    }
}
