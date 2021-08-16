package fast.cloud.nacos.pojo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class BizPerson implements Person, BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean {
    private Animal animal;

    @Override
    public void service() {
        animal.use();
    }

    @Override
    @Autowired
    @Qualifier("cat")
    public void setAnimal(Animal animal) {
        System.out.println("延迟依赖注入");
        this.animal = animal;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println(this.getClass().getSimpleName() + "调用BeanNameAware的setBeanName方法");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println(this.getClass().getSimpleName() + "调用BeanFactoryAware的setBeanFactory方法");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println(this.getClass().getSimpleName() + "调用ApplicationContextAware的setApplicationContext方法");
    }

    @PostConstruct
    public void init() {
        System.out.println(this.getClass().getSimpleName() + "调用@PostConstruct自定义的初始化方法");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(this.getClass().getSimpleName() + "调用InitializingBean的afterPropertiesSet方法");
    }

    @PreDestroy
    public void destroy1() {
        System.out.println(this.getClass().getSimpleName() + "调用@PreDestroy自定义的销毁方法");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println(this.getClass().getSimpleName() + "调用DisposableBean的destroy方法");
    }
}
