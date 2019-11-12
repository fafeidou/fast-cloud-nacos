package fast.boot.autoconfigure.configuration;

import org.springframework.context.annotation.Bean;

public class HelloWorldConfiguration {

    @Bean
    public String helloWorld() {
        return "hello world";
    }
}
