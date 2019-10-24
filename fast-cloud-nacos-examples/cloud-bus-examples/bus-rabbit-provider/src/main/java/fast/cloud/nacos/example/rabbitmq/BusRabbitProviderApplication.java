package fast.cloud.nacos.example.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(HelloBinding.class)
public class BusRabbitProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusRabbitProviderApplication.class, args);
    }

}
