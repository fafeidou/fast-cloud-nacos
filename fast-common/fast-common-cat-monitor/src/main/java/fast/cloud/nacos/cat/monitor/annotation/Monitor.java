package fast.cloud.nacos.cat.monitor.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Monitor {
    String type() default "";

    String name() default "";

    boolean isSaveParam() default true;
}
