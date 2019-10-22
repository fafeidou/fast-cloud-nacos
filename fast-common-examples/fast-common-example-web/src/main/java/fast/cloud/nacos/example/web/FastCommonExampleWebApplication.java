package fast.cloud.nacos.example.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@SpringBootApplication
@ComponentScan("fast.cloud.nacos")
public class FastCommonExampleWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastCommonExampleWebApplication.class, args);
    }

    /**
     * MessageSource
     */
    @Resource
    private MessageSource messageSource;

    /**
     * Validation message i18n
     * @return Validator
     */
    @NotNull
    @Bean
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(this.messageSource);
        return validator;
    }

}
