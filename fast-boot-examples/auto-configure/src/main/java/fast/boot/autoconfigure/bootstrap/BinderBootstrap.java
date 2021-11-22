package fast.boot.autoconfigure.bootstrap;

import fast.boot.autoconfigure.bind.BinderTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author qinfuxiang
 */
public class BinderBootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(EnableHelloWorldBootstrap.class, args);
        ConfigurableEnvironment environment = context.getBean(ConfigurableEnvironment.class);
        //配置properties 信息
        BindResult<Properties> propertiesBindResult = Binder.get(environment)
                .bind("hello-world.properties", Properties.class);
        BindResult<Map> mapBindResult = Binder.get(environment)
                .bind("hello-world.properties", Map.class);
        BindResult<BinderTest> binderTestBindResult = Binder.get(environment).bind("binder.test", Bindable.of(BinderTest.class));
        BindResult<List<String>> resultList = Binder.get(environment).bind("binder.test2.list", Bindable.listOf(String.class));
        System.out.println();
    }

}
