package fast.cloud.nacos.common.model.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class JsonUtils {

    private final static Logger logger = LoggerFactory
            .getLogger(JsonUtils.class);

    private static ObjectMapper jsonMapper = new ObjectMapper();

    public static <T> T toObject(String value, Class<T> clazz) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return jsonMapper.readValue(value, clazz);
        } catch (Exception e) {
            logger.info("", e);
            return null;
        }
    }

    /**
     * 转成Json格式数据
     *
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        if (Objects.isNull(obj)) {
            return "";
        }
        try {

            return jsonMapper.writeValueAsString(obj);
        } catch (Exception e) {

            logger.info("", e);
            return null;
        }
    }

    public static void main(String[] args) {

        String s = "祝你考出好成绩!";
        System.out.println(s.length());
    }
}
