package fast.cloud.nacos.tenant.dynamic.mybatis;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    /**
     * 数据源key值
     *
     * @return
     */
    String value();

}
