package fast.cloud.nacos.grpc.starter.annotation;

import java.lang.annotation.*;

/**
 * @author qinfuxiang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface GrpcReference {

    /**
     * appId
     */
    String appId() default "";

    /**
     * https
     */
    boolean https() default false;

    /**
     * oneWay
     * If the method has the same properties is ignored
     */
    boolean oneWay() default false;

    /**
     * tcp connectionTimeoutMillis
     * If the method has the same attributes  is ignored
     */
    int connectionTimeoutMillis() default 0;

    /**
     * tcp stream readTimeoutMillis
     * If the method has the same attributes  is ignored
     */
    int readTimeoutMillis() default 0;

}
