package fast.cloud.nacos.example.web.annotation;

import fast.cloud.nacos.example.web.enums.SecurityLevel;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

/**
 * @author qinfuxiang
 * @Date 2020/6/5 13:39
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpApi {
    @AliasFor("value")
    String name() default "";

    @AliasFor("name")
    String value() default "";

    String desc() default "";

    SecurityLevel security() default SecurityLevel.UserLogin;
}