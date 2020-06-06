package fast.cloud.nacos.example.web.enums.commons;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RegisterEnum {

    /**
     * 枚举名称
     */
    String value();

    /**
     * 枚举中文名
     */
    String desc();

    /**
     * 枚举使用位置
     */
    String[] usedLocation() default "";

    /**
     * 枚举使用页面
     */
    String[] usedUrl() default "";
}
