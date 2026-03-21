package com.yildiz.teamsync.config;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    private final RateLimitingService rateLimitingService;

    public RateLimitingInterceptor(RateLimitingService rateLimitingService) {
        this.rateLimitingService = rateLimitingService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Einfache IP-Ermittlung
        String ipAddress = request.getRemoteAddr();
        
        // Bei Proxies wie Cloudflare oder Nginx/Heroku müsste man X-Forwarded-For nutzen:
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            ipAddress = xForwardedFor.split(",")[0];
        }

        Bucket bucket = rateLimitingService.resolveBucket(ipAddress);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            // Header mitschicken (guter Standard)
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            // Limit erreicht!
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            throw new RateLimitExceededException(
                "Zu viele Anfragen! Dein Limit ist erschöpft. Bitte warte " + waitForRefill + " Sekunden."
            );
        }
    }
}
