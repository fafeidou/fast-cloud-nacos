package fast.cloud.nacos.cat.monitor.prometheus;


import fast.cloud.nacos.cat.monitor.constants.HttpMonitorConstant;
import fast.cloud.nacos.cat.monitor.enhance.RestTemplateInstrumenter;
import fast.cloud.nacos.cat.monitor.util.SpringBeanUtil;
import feign.Response;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

public class HttpClientMonitor {
    private static final HttpClientMonitor instance = new HttpClientMonitor();
    private final ThreadLocal<Timer.Sample> timeSample = new ThreadLocal();
    public static final String STAR = "*";
    public static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

    private HttpClientMonitor() {
    }

    public static HttpClientMonitor getInstance() {
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
            feign.Request  request = (feign.Request) ths;
            Response response = (Response) result;
            Tag ret = HttpMonitorConstant.RET_TAG;
            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(request.url()).build();
            Tags serverDomainTag = generateTagsForRoute(uriComponents,request.headers());
            int statusCode = statusCode(response);
            Tag status = Tag.of("status", String.valueOf(statusCode));
            Tag error = Tag.of("error", error(statusCode));
            if (null != t) {
                status = HttpMonitorConstant.NONE_STATUS;
                error = Tag.of("error", "1");
                ret = HttpMonitorConstant.RET_FAIL;
            }

            Iterable<Tag> tags = Tags.of(serverDomainTag).and(status, error, ret);

            getInstance().getTime().stop(BaseMonitorClient.histogramTimerBuilder(HttpMonitorConstant.HTTP_CLIENT_METER_NAME).description("Duration of HttpClient request execution").minimumExpectedValue(Duration.ofSeconds(1L)).maximumExpectedValue(Duration.ofSeconds(1L)).tags(tags).register(SpringBeanUtil.getBean(MeterRegistry.class)));
        } catch (Throwable throwable) {
            LoggerFactory.getLogger(RestTemplateInstrumenter.class).warn("HttpClient 监控异常:" + throwable.getMessage());
        } finally {
            clean();
        }

    }

    public static Tags generateTagsForRoute(UriComponents uriComponents,Map<String, Collection<String>> headers) {
        String targetHost = uriComponents.getHost();
        String serverUrlApi = uriComponents.getPath();
        Collection<String> contexts = headers.get("CONTEXT_KEY");
        return Tags.of(HttpMonitorConstant.SERVER_URL_API, handleNumber(serverUrlApi), HttpMonitorConstant.SERVER_DOMAIN, targetHost,"trace_id", CollectionUtils.isEmpty(contexts)?"-":contexts.iterator().next());
    }

    private static String error(int statusCode) {
        return statusCode >= 400 && statusCode <= 600 ? "1" : "0";
    }

    private static int statusCode(Response response) {
        try {
            return response.status();
        } catch (Throwable throwable) {
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }

    public static String handleNumber(String tagString) {
        return NUMBER_PATTERN.matcher(tagString).replaceAll(STAR);
    }
}
