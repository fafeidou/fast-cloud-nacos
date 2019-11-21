package fast.cloud.nacos.webflux.utils;

public class Util {
    public static String emptyToNull(String string) {
        return string == null || string.isEmpty() ? null : string;
    }
}
