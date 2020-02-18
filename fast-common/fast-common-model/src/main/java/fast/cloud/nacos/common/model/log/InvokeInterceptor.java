//package fast.cloud.nacos.common.model.log;
//
//import fast.cloud.nacos.common.model.beans.context.ProjectContext;
//import org.slf4j.MDC;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Objects;
//import java.util.Random;
//
//public class InvokeInterceptor implements HandlerInterceptor {
//
//    private static final String TRACE = "trace";
//    private static final Random RANDOM = new Random();
//    public static final String TRACE_ID = "trace";
//    public static final String SPAN_ID = "span";
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        String contextString = request.getHeader(ProjectContext.CONTEXT_KEY);
//        if (Objects.nonNull(contextString)) {
//            ProjectContext.fromString(contextString);
//        } else {
//            // 无内容时，也自动初始化
//            ProjectContext.initContext();
//        }
//        initLog();
//        return true;
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        MDC.remove(TRACE);
//    }
//
//    private long genLogId() {
//        return Math.round(((System.currentTimeMillis() % 86400000L) + RANDOM.nextDouble()) * 100000.0D);
//    }
//
//    public static void initLog() {
//        MDC.put(TRACE_ID, ProjectContext.getContext().getTraceId());
//        MDC.put(SPAN_ID, ProjectContext.getContext().getSpanId());
//    }
//}
