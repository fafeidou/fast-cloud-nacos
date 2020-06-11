package fast.cloud.nacos.grpc.starter.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import fast.cloud.nacos.grpc.starter.constant.SerializeType;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;

@Documented
@Inherited
@Retention(RUNTIME)
public @interface GrpcService {

    /**
     * 远程服务名
     */
    String server() default "";

    /**
     * 序列化工具实现类
     */
    SerializeType[] serialization() default {};

}