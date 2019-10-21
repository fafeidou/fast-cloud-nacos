package fast.cloud.nacos.example.web.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.validation.Validator;
//import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//
//@Configuration
//public class ValidatorConfiguration extends WebMvcConfigurationSupport {
//    @Autowired
//    private MessageSource messageSource;
//
//    @Override
//    public Validator getValidator() {
//        return validator();
//    }
//
//    @Bean
//    public Validator validator() {
//        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
//        validator.setValidationMessageSource(messageSource);
//        return validator;
//    }
//}

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidatorConfiguration {
    public ResourceBundleMessageSource getMessageSource() throws Exception {
        ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
        rbms.setDefaultEncoding("UTF-8");
        rbms.setBasenames("i18n/message");
        return rbms;
    }

    @Bean
    public Validator getValidator() throws Exception {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(getMessageSource());
        return validator;
    }
}
