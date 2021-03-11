package fast.cloud.nacos.orderservicetcc.utils;

import java.util.UUID;

/**
 * @author qfx
 */
public class UUIDUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static Integer getUUIDInOrderId() {
        Integer orderId = UUID.randomUUID().toString().hashCode();
        orderId = orderId < 0 ? -orderId : orderId;
        return orderId;
    }
}