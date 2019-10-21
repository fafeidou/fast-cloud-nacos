package fast.cloud.nacos.example.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("fast.cloud.nacos")
public class FastCommonExampleWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(FastCommonExampleWebApplication.class, args);
	}

}
