package fast.boot.autoconfigure.postprocessor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Configuration
public class InstantiationAwareBeanPostProcessorBootstrap {
    public static void main(String[] args) {
//        ConfigurableApplicationContext context = SpringApplication.run(InstantiationAwareBeanPostProcessorBootstrap.class, args);
//        User bean = context.getBean(User.class);
//        System.out.println(bean);
//        context.registerShutdownHook();
        ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        User user = ac.getBean(User.class);
        System.out.println(user);
        // 关闭销毁
        ac.registerShutdownHook();
    }

    @Bean
    public User user() {
        User user = new User();
        user.setName("aoao");
        return user;
    }

//    @Bean
//    public MyInstantiationAwareBeanPostProcessor myInstantiationAwareBeanPostProcessor() {
//        return new MyInstantiationAwareBeanPostProcessor();
//    }

}
