package fast.cloud.nacos.grpc.starter.constant;

import fast.cloud.nacos.grpc.starter.service.impl.FastJSONSerializeService;
import fast.cloud.nacos.grpc.starter.service.impl.JacksonSerializeService;
import fast.cloud.nacos.grpc.starter.service.impl.ProtoStuffSerializeService;
import fast.cloud.nacos.grpc.starter.service.impl.SofaHessianSerializeService;
import java.util.HashMap;
import java.util.Map;

/**
 * 序列化类型枚举
 */
public enum SerializeType {

    SOFAHESSIAN(1, SofaHessianSerializeService.class),
    PROTOSTUFF(2, ProtoStuffSerializeService.class),
    FASTJSON(3, FastJSONSerializeService.class),
    JACKSON(4, JacksonSerializeService .class);

    private static Map<Integer, SerializeType> enumMap = new HashMap<>();

    static {
        for (SerializeType serializeType : SerializeType.values()) {
            enumMap.put(serializeType.value, serializeType);
        }
    }

    private int value;

    private Class clazz;

    SerializeType(int value, Class clazz){
        this.clazz = clazz;
        this.value = value;
    }

    public static SerializeType getSerializeTypeByValue(int value){
        return enumMap.get(value);
    }

    public int getValue() {
        return value;
    }

    public Class getClazz() {
        return clazz;
    }
}
