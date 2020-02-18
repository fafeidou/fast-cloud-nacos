package fast.cloud.nacos.common.model.beans.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fast.cloud.nacos.common.model.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Random;

@Getter
@Setter
public class ProjectContext {

    public static final String CONTEXT_KEY = "CONTEXT_KEY";
    private static final String DEFAULT_SPAN = "1";
    private static final Random RANDOM = new Random();

    /**
     * 每次请求唯一记录值
     */
    private String traceId;

    /**
     * 一次请求的多次处理唯一标记
     */
    private String spanId;

    /**
     * 请求ip
     */
    private String ip;

    private static ThreadLocal<ProjectContext> LOCAL = new TransmittableThreadLocal<>();

    public static ProjectContext getContext() {
        ProjectContext context = LOCAL.get();
        if (Objects.isNull(context)) {
            context = new ProjectContext();
        }

        return context;
    }

    static void nextSpan() {
        if (Objects.isNull(getContext())) {
            initContext();
            return;
        }
        if (Objects.isNull(getContext().getSpanId())) {
            getContext().setSpanId(DEFAULT_SPAN);
            return;
        }

        // 获取当前的spanId
        String span = getContext().getSpanId();
        if (span.endsWith(".")) {
            span = span.substring(0, span.length() - 1);
        }
        // 找到切割位置
        int p = span.lastIndexOf(".");
        String last = span.substring(p + 1);
        // 最后需要自增的原数据
        int lastId = Integer.parseInt(last);
        // 完成自增并设置设置到spanId中
        if (p < 0) {
            getContext().setSpanId(String.valueOf(lastId + 1));
        } else {
            getContext().setSpanId(span.substring(0, p) + (lastId + 1));
        }
    }

    /**
     * 透传上下文
     *
     * @param contextString 被序列化的上下文字符串
     */
    public static void fromString(String contextString) {
        ProjectContext context = JsonUtils.toObject(contextString, ProjectContext.class);

        fromContext(context);
    }

    public static void fromContext(ProjectContext context) {
        LOCAL.set(context);

        nextSpan();
    }

    static void setContext(ProjectContext context) {
        LOCAL.set(context);
    }

    public static void initContext(String ip) {
        initContext();

        ProjectContext context = getContext();

        context.setIp(ip);

        setContext(context);
    }

    public static void initContext() {
        ProjectContext context = new ProjectContext();
        context.setTraceId(String.valueOf(genLogId()));
        context.setSpanId(DEFAULT_SPAN);

        setContext(context);
    }

    public void release() {

    }

    public static long genLogId() {
        return Math.round(((System.currentTimeMillis() % 86400000L) + RANDOM.nextDouble()) * 100000.0D);
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 忽略null打印日志
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
        }

        return "";
    }
}
