package fast.cloud.nacos.grpc.starter.util;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

@Slf4j
public class ProtobufUtils {

    private final static Objenesis OBJENESIS = new ObjenesisStd(true);

    private ProtobufUtils() {
    }

    private static class SingletonHolder {

        final static ProtobufUtils INSTANCE = new ProtobufUtils();
    }

    public static ProtobufUtils getInstance() {
        return ProtobufUtils.SingletonHolder.INSTANCE;
    }

    // 缓存 schema 对象的 map
    private static Map<Class<?>, RuntimeSchema<?>> cachedSchema = new ConcurrentHashMap<>();

    /**
     * 根据获取相应类型的schema方法
     */
    @SuppressWarnings({"unchecked", "unused"})
    private static <T> RuntimeSchema<T> getSchema(Class<T> clazz) {
        RuntimeSchema<T> schema = (RuntimeSchema<T>) cachedSchema.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(clazz);
            cachedSchema.put(clazz, schema);
        }
        return schema;
    }

    /**
     * 序列化方法，将对象序列化为字节数组（对象 ---> 字节数组）
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        RuntimeSchema<T> schema = getSchema(clazz);
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
    }

    public static <T> T deserialize2(byte[] data, Class<T> clazz) {
        RuntimeSchema<T> schema = RuntimeSchema.createFrom(clazz);
        T message = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, message, schema);

        return message;
    }
    /**
     * 反序列化方法，将字节数组反序列化为对象（字节数组 ---> 对象）
     */
    public static <T> T deserialize(byte[] param, Class<T> clazz) {
        T object = OBJENESIS.newInstance(clazz);
        Schema<T> schema = getSchema(clazz);
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(param)) {
            ProtobufIOUtil.mergeFrom(inputStream, object, schema);
            return object;
        } catch (IOException e) {
            log.error("反序列化对象失败", e);
        }
        return null;
    }

}
