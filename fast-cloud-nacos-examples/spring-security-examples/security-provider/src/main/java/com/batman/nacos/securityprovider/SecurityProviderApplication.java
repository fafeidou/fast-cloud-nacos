package com.batman.nacos.securityprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.batman")
public class SecurityProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityProviderApplication.class, args);
    }
}
