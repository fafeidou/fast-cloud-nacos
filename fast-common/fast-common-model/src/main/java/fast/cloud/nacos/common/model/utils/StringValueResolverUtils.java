package fast.cloud.nacos.common.model.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringValueResolver;

public class StringValueResolverUtils {

    private static Logger logger = LoggerFactory.getLogger(StringValueResolverUtils.class);

    private static StringValueResolver resolver;

    public static void setResolver(StringValueResolver resolver) {
        StringValueResolverUtils.resolver = resolver;
    }

    public static StringValueResolver getResolver() {
        return resolver;
    }

    public static String resolve(String value) {
        try {
            return resolver.resolveStringValue(value);
        } catch (Exception e) {
            logger.warn("{} not configured！", value);
        }
        return "";
    }
}