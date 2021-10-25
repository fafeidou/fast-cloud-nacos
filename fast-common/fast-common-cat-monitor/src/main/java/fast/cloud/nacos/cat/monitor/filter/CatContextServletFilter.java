package fast.cloud.nacos.cat.monitor.filter;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.spi.MessageTree;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fast.cloud.nacos.cat.monitor.bootstrap.ContextStrategyImpl;
import fast.cloud.nacos.cat.monitor.common.CatContextImpl;
import fast.cloud.nacos.cat.monitor.common.MonitorResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CatContextServletFilter implements Filter {
    private ObjectMapper objectMapper;

    public CatContextServletFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,
        ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        MonitorResponse res = new MonitorResponse(response);
        String uri = request.getRequestURI();
        Transaction filterTransaction = Cat.newTransaction("URL", uri);
        String originServiceName;
        if (null != request.getHeader("X-ROOT-MESSAGE-ID")) {
            CatContextImpl catContext = new CatContextImpl();
            catContext.addProperty("_catRootMessageId", request.getHeader("X-ROOT-MESSAGE-ID"));
            catContext.addProperty("_catParentMessageId", request.getHeader("X-ROOT-PARENT-ID"));
            catContext.addProperty("_catChildMessageId", request.getHeader("X-ROOT-CHILD-ID"));
            Cat.logRemoteCallServer(catContext);
            MDC.put("traceId", catContext.getProperty("_catRootMessageId"));
        } else if (Cat.isEnabled()) {
            originServiceName = Cat.getCurrentMessageId();
            MessageTree tree = Cat.getManager().getThreadLocalMessageTree();
            MDC.put("traceId", originServiceName);
            tree.setRootMessageId(originServiceName);
            tree.setParentMessageId(originServiceName);
        }

        try {
            Cat.logEvent("URL.method", request.getMethod(), "0", request.getRequestURL().toString());
            Cat.logEvent("URL.client", request.getRemoteHost());
            originServiceName = request.getHeader("service-name");
            if (!StringUtils.isEmpty(originServiceName)) {
                Cat.logEvent("Service", originServiceName);
            }

            this.customizeStatus(filterTransaction, request, res);
            filterChain.doFilter(requestWrapper, servletResponse);
            filterTransaction.setSuccessStatus();
        } catch (Exception e) {
            filterTransaction.setStatus(e);
            Cat.logError("请求体：" + this.getRequestBody(requestWrapper), e);
            throw e;
        } finally {
            filterTransaction.complete();
            ContextStrategyImpl.contextThreadLocal.remove();
            MDC.remove("traceId");
        }

    }

    @Override
    public void destroy() {
    }

    private void customizeStatus(Transaction t, HttpServletRequest req, MonitorResponse res) {
        Object catStatus = req.getAttribute("cat-state");
        if (catStatus != null) {
            t.setStatus(catStatus.toString());
        } else if (CatFilterConfigure.analysisEnable) {
            try {
                byte[] bytes = res.getBody();
                JsonNode body = this.objectMapper.readTree(bytes);
                JsonNode success = body.get("success");
                if (success.asBoolean()) {
                    t.setStatus("0");
                } else {
                    JsonNode codeNode = body.get("code");
                    int code = codeNode.asInt(0);
                    t.setStatus(String.valueOf(code));
                    JsonNode msg = body.get("msg");
                    t.addData("msg", msg);
                }
            } catch (Exception var11) {
                t.setStatus("0");
            }
        } else {
            t.setStatus("0");
        }

    }

    private String getRequestBody(ContentCachingRequestWrapper req) {
        try {
            return IOUtils.toString(req.getContentAsByteArray(), "UTF-8");
        } catch (IOException var3) {
            return "";
        }
    }
}