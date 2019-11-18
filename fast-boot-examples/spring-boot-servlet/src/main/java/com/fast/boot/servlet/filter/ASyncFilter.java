package com.fast.boot.servlet.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(asyncSupported = true, urlPatterns = {"/AsyncServlet", "/NonBlockingServlet"})
public class ASyncFilter implements Filter {

    public void init(FilterConfig filterConfig) {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);

    }

    public void destroy() {

    }

}
