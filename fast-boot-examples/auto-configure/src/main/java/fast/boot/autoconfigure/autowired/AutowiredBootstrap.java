package fast.boot.autoconfigure.autowired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ComponentScan
@Configuration
public class AutowiredBootstrap {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AutowiredBootstrap.class);
        A a = applicationContext.getBean(A.class);
        a.test();
        applicationContext.registerShutdownHook();
    }

    @Component
    public class A {

        @Autowired
        private B b;

        public void test() {
            System.out.println(b);
        }

    }

    @Component
    public class B {
    }

}
