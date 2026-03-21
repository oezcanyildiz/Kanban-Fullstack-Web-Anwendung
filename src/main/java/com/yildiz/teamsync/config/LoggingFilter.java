package com.yildiz.teamsync.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        logger.info("INCOMING REQUEST: {} {} from {}", req.getMethod(), req.getRequestURI(), req.getRemoteAddr());
        logger.info("AUTH HEADER: {}", req.getHeader("Authorization"));
        chain.doFilter(request, response);
    }
}
