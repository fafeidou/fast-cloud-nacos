package fast.cloud.nacos.cat.monitor.constants;

import io.micrometer.core.instrument.Tag;

public class HttpMonitorConstant {
    public static String METER_NAME = "service_http_call";
    public static String HTTP_CLIENT_METER_NAME = "service_http_client_call";
    public static String METER_DESC = "http call latency in seconds";
    public static String FROM_APP_ID = "from_appid";
    public static String TO_APPID = "to_appid";
    public static String CLIENT_IP = "client_ip";
    public static String SERVER_DOMAIN = "server_domain";
    public static String SERVER_URL_API = "server_url_api";
    public static String TRACE_ID = "trace_id";
    public static Tag STATUS_CLIENT_ERROR = Tag.of("status", "CLIENT_ERROR");
    public static Tag NONE_STATUS = Tag.of("status", "-");
    public static Tag SUCCESS_STATUS = Tag.of("status", "200");
    public static Tag SUCCESS_TAG = Tag.of("error", "0");
    public static Tag UNKNOW_ERROR = Tag.of("error", "-");
    public static Tag ERROR_TAG = Tag.of("error", "1");
    public static Tag RET_TAG = Tag.of("ret", "0");
    public static Tag RET_FAIL = Tag.of("ret", "1");
}
