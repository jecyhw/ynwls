package com.cn.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by jecyhw on 2014/10/29.
 */
public class SetCharacterEncodingFilter implements Filter {
    protected String encoding = null;
    public void destroy() {
        this.encoding = null;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        req.setCharacterEncoding(this.encoding);
        resp.setContentType("text/html;charset=" + this.encoding);
        resp.setCharacterEncoding(this.encoding);
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        this.encoding = config.getInitParameter("encoding");
    }

}
