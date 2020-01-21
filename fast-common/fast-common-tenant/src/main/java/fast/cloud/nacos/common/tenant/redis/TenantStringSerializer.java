package fast.cloud.nacos.common.tenant.redis;

import fast.cloud.nacos.common.tenant.context.TenantStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

public class TenantStringSerializer implements RedisSerializer<String> {

    private final Charset charset;

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantStringSerializer.class);


    public TenantStringSerializer() {
        this(Charset.forName("UTF8"));
    }

    public TenantStringSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    private String tenantNameSpace() {
        if (TenantStore.getTenantId() != null) {
            String tenantId = TenantStore.getTenantId();
            return "tenant:" + tenantId + ":";
        } else {
            return "";
        }
    }

    private String platformNameSpace() {
        return "platform:";
    }

    @Override
    public String deserialize(byte[] bytes) {
        String keyPrefix = tenantNameSpace();
        String saveKey = new String(bytes, charset);
        int indexOf = saveKey.indexOf(keyPrefix);
        if (indexOf > 0) {
            LOGGER.debug("key miss prefix");
        } else {
            saveKey = saveKey.substring(indexOf);
        }
        LOGGER.debug("saveKey:{}", saveKey);
        return (saveKey.getBytes() == null ? null : saveKey);
    }

    @Override
    public byte[] serialize(String string) {
        String keyPrefix = tenantNameSpace();
        String key = keyPrefix + string;
        LOGGER.debug("key:{},getBytes:{}", key, key.getBytes(charset));
        return (key == null ? null : key.getBytes(charset));
    }


}
