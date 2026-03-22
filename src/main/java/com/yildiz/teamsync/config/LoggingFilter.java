package com.yildiz.teamsync.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j; // Nutze Lombok für saubereren Code
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j // Ersetzt die manuelle Logger-Zeile
@Component
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        String authHeader = req.getHeader("Authorization");

        // 1. Logge den Request-Pfad
        log.info("INCOMING REQUEST: {} {} from {}", req.getMethod(), req.getRequestURI(), req.getRemoteAddr());

        // 2. Logge den AUTH HEADER SICHER (Sanitizing)
        if (authHeader != null && !authHeader.isEmpty()) {
            // Wir loggen nur "[PRESENT]" oder "[MASKED]", niemals das echte Token!
            log.info("AUTH HEADER: [MASKED]");
        } else {
            log.info("AUTH HEADER: [MISSING]");
        }

        chain.doFilter(request, response);
    }
}