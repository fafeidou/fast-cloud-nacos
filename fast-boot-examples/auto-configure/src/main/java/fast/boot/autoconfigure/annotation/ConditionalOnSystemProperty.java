package fast.boot.autoconfigure.annotation;


import fast.boot.autoconfigure.condition.OnSystemPropertyCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(OnSystemPropertyCondition.class)
public @interface ConditionalOnSystemProperty {

    /**
     * Java 系统属性名称
     *
     * @return
     */
    String name();

    /**
     * Java 系统属性值
     *
     * @return
     */
    String value();
}
