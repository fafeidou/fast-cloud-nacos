package fast.cloud.nacos.cat.monitor.prometheus;


import fast.cloud.nacos.cat.monitor.constants.HttpMonitorConstant;
import fast.cloud.nacos.cat.monitor.enhance.RestTemplateInstrumenter;
import fast.cloud.nacos.cat.monitor.util.SpringBeanUtil;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;

import java.net.URI;
import java.time.Duration;
import java.util.regex.Pattern;

public class RestTemplateMonitor {
    private static final RestTemplateMonitor instance = new RestTemplateMonitor();
    private final ThreadLocal<Timer.Sample> timeSample = new ThreadLocal();
    public static final String STAR = "*";
    public static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

    private RestTemplateMonitor() {
    }

    public static RestTemplateMonitor getInstance() {
        return instance;
    }

    public static void start() {
        try {
            try {
                getInstance().startTime();
            } catch (Throwable throwable) {

            }
        } finally {
        }
    }

    public static void clean() {
        getInstance().cleanTime();
    }

    private void startTime() {
        if (null == this.timeSample.get()) {
            Timer.Sample start = Timer.start();
            this.timeSample.set(start);
        }

    }

    public Timer.Sample getTime() {
        if (null == this.timeSample.get()) {
            Timer.Sample start = Timer.start();
            this.timeSample.set(start);
            return start;
        } else {
            return this.timeSample.get();
        }
    }

    private void cleanTime() {
        this.timeSample.remove();
    }

    public static void metric(Object ths, Object result, Throwable t) {
        try {
            ClientHttpResponse response = null;
            ClientHttpRequest request = (ClientHttpRequest) ths;
            response = (ClientHttpResponse) result;
            Tag ret = HttpMonitorConstant.RET_TAG;
            Tags serverDomainTag = generateTagsForRoute(request.getURI());
            int statusCode = statusCode(response);
            Tag status = Tag.of("status", String.valueOf(statusCode));
            Tag error = Tag.of("error", error(statusCode));
            if (null != t) {
                status = HttpMonitorConstant.NONE_STATUS;
                error = Tag.of("error", "1");
                ret = HttpMonitorConstant.RET_FAIL;
            }

            Iterable<Tag> tags = Tags.of(serverDomainTag).and(new Tag[]{status, error, ret});

            getInstance().getTime().stop(BaseMonitorClient.histogramTimerBuilder(HttpMonitorConstant.METER_NAME).description("Duration of RestTemplate request execution").minimumExpectedValue(Duration.ofSeconds(1L)).maximumExpectedValue(Duration.ofSeconds(1L)).tags(tags).register(SpringBeanUtil.getBean(MeterRegistry.class)));
        } catch (Throwable var15) {
            LoggerFactory.getLogger(RestTemplateInstrumenter.class).warn("RestTemplate 监控异常:" + var15.getMessage());
        } finally {
            clean();
        }

    }

    public static Tags generateTagsForRoute(URI url) {
        String targetHost = url.getHost();
        String serverUrlApi = url.getPath();
        return Tags.of(new String[]{HttpMonitorConstant.SERVER_URL_API, handleNumber(serverUrlApi), HttpMonitorConstant.SERVER_DOMAIN, targetHost});
    }

    private static String error(int statusCode) {
        return statusCode >= 400 && statusCode <= 600 ? "1" : "0";
    }

    private static int statusCode(ClientHttpResponse response) {
        try {
            return response.getRawStatusCode();
        } catch (Throwable throwable) {
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }

    public static String handleNumber(String tagString) {
        return NUMBER_PATTERN.matcher(tagString).replaceAll(STAR);
    }
}
