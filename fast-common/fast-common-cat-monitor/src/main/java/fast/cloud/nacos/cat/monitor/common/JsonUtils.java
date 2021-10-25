package fast.cloud.nacos.cat.monitor.common;


import com.dianping.cat.Cat;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class JsonUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    public JsonUtils() {
    }

    public static String toString(Object obj) {
        return toJson(obj);
    }

    public static String toJson(Object obj) {
        try {
            StringWriter writer = new StringWriter();
            mapper.writeValue(writer, obj);
            return writer.toString();
        } catch (Exception var2) {
            Cat.logError("序列化对象【" + obj + "】时出错", var2);
            return "";
        }
    }

    public static <T> T toBean(Class<T> entityClass, String jsonString) {
        try {
            return mapper.readValue(jsonString, entityClass);
        } catch (Exception var3) {
            throw new RuntimeException("JSON【" + jsonString + "】转对象时出错", var3);
        }
    }

    public static String getJsonSuccess(Object obj) {
        String jsonString = null;
        if (obj == null) {
            jsonString = "{\"success\":true}";
        } else {
            jsonString = "{\"success\":true,\"data\":" + toJson(obj) + "}";
        }

        return jsonString;
    }

    public static String getJsonSuccess(Object obj, String message) {
        if (obj == null) {
            return "{\"success\":true,\"message\":\"" + message + "\"}";
        } else {
            try {
                Map<String, Object> map = new HashMap();
                map.put("success", true);
                return "{\"success\":true," + toString(obj) + ",\"message\":\"" + message + "\"}";
            } catch (Exception var3) {
                throw new RuntimeException("序列化对象【" + obj + "】时出错", var3);
            }
        }
    }

    public static String getJsonError(Object obj) {
        return getJsonError(obj, (String)null);
    }

    public static String getJsonError(Object obj, String message) {
        if (obj == null) {
            return "{\"success\":false,\"message\":\"" + message + "\"}";
        } else {
            try {
                obj = parseIfException(obj);
                return "{\"success\":false,\"data\":" + toString(obj) + ",\"message\":\"" + message + "\"}";
            } catch (Exception var3) {
                throw new RuntimeException("序列化对象【" + obj + "】时出错", var3);
            }
        }
    }

    public static Object parseIfException(Object obj) {
        return obj instanceof Exception ? getErrorMessage((Exception)obj, (String)null) : obj;
    }

    public static String getErrorMessage(Exception e, String defaultMessage) {
        return defaultMessage != null ? defaultMessage : null;
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }
}
