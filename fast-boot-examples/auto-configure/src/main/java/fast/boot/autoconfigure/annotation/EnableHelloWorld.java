package fast.boot.autoconfigure.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
//@Import(HelloWorldConfiguration.class)
//@Import(HelloWorldImportSelector.class)
//@Import(HelloWorldImportBeanDefinitionRegistrar.class)
//@Import(HelloWorldBeanFactoryPostProcessor.class)
public @interface EnableHelloWorld {
}
