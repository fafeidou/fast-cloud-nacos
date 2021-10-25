package fast.cloud.nacos.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomService {
    String value();

    String moduleName() default "";

    String name() default "";

    String contextId() default "";

    String appId() default "";

    String appHost() default "";

    String appPort() default "";

    boolean isHttps() default false;
}

