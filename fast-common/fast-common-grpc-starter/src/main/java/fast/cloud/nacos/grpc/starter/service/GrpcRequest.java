package fast.cloud.nacos.grpc.starter.service;

import java.io.Serializable;
import lombok.Data;

@Data
public class GrpcRequest implements Serializable {

    private static final long serialVersionUID = 4729940126314117605L;

    /**
     * 接口
     */
    private String clazz;

    /**
     * 方法
     */
    private String method;

    /**
     * service 方法参数
     */
    private Object[] args;

}
