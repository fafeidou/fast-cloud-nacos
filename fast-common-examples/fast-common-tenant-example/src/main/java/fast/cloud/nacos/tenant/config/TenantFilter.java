package fast.cloud.nacos.tenant.config;

import fast.cloud.nacos.tenant.constants.TenantConstant;
import fast.cloud.nacos.tenant.context.TenantStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class TenantFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantFilter.class);

    @Override
    public void init(FilterConfig config) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LOGGER.debug("request Headers Info data: {} ", getHeadersInfo(request));

        String tenantId = request.getHeader(TenantConstant.TENANT_NAME);
        try {
            TenantStore.setTenantId(tenantId);
            chain.doFilter(servletRequest, servletResponse);
        } finally {
            TenantStore.clear();
        }
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    @Override
    public void destroy() {
    }

}
