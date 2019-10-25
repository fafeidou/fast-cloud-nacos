package fast.cloud.nacos.examples.rocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;

@SpringBootApplication
@EnableBinding({Sink.class})
public class BusRocketConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusRocketConsumerApplication.class, args);
    }


}
