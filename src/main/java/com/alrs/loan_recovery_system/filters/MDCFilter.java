package com.alrs.loan_recovery_system.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class MDCFilter extends OncePerRequestFilter {

    private static final String TRACE_ID = "traceId";
//    private static final String SPAN_ID = "spanId";
//    private static final String USER_ID = "userId";
//    private static final String SESSION_ID = "sessionId";
    private static final String REQUEST_URI = "requestUri";
    private static final String REQUEST_METHOD = "requestMethod";
//    private static final String USER_AGENT = "userAgent";
//    private static final String CLIENT_IP = "clientIp";
    private static final String REQUEST_ID = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        try {
            // Generate or extract trace information
            String traceId = getOrGenerateTraceId(request);
            String spanId = UUID.randomUUID().toString().substring(0, 8);
            String requestId = UUID.randomUUID().toString();

            // Set MDC values
            MDC.put(TRACE_ID, traceId);
//            MDC.put(SPAN_ID, spanId);
            MDC.put(REQUEST_ID, requestId);
            MDC.put(REQUEST_URI, request.getRequestURI());
            MDC.put(REQUEST_METHOD, request.getMethod());
//            MDC.put(USER_AGENT, maskUserAgent(request.getHeader("User-Agent")));
//            MDC.put(CLIENT_IP, getClientIpAddress(request));

            // Set user information if available
//            Principal principal = request.getUserPrincipal();
//            if (principal != null) {
//                MDC.put(USER_ID, maskUserId(principal.getName()));
//            }

            // Set session information
//            HttpSession session = request.getSession(false);
//            if (session != null) {
//                MDC.put(SESSION_ID, session.getId());
//            }

            // Add response headers for tracing
            response.setHeader("X-Trace-Id", traceId);
            response.setHeader("X-Request-Id", requestId);

            // Log request start
            log.info("Request started: {} {}", request.getMethod(), request.getRequestURI());

            // Continue with the filter chain
            filterChain.doFilter(request, response);

        } finally {
            // Log request completion
            long duration = System.currentTimeMillis() - startTime;
            log.info("Request completed in {}ms with status: {}", duration, response.getStatus());

            // Clean up MDC
            MDC.clear();
        }
    }

    private String getOrGenerateTraceId(HttpServletRequest request) {
        // Check for existing trace ID in headers (from upstream services)
        String traceId = request.getHeader("X-Trace-Id");
        if (traceId == null || traceId.trim().isEmpty()) {
            traceId = request.getHeader("X-Request-ID");
        }
        if (traceId == null || traceId.trim().isEmpty()) {
            traceId = UUID.randomUUID().toString();
        }
        return traceId;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return maskIpAddress(xForwardedFor.split(",")[0].trim());
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return maskIpAddress(xRealIp);
        }

        return maskIpAddress(request.getRemoteAddr());
    }

    private String maskUserId(String userId) {
        if (userId == null || userId.length() <= 4) {
            return "***";
        }
        return userId.substring(0, 2) + "***" + userId.substring(userId.length() - 2);
    }

    private String maskIpAddress(String ip) {
        if (ip == null) {
            return "unknown";
        }
        String[] parts = ip.split("\\.");
        if (parts.length == 4) {
            return parts[0] + "." + parts[1] + ".xxx.xxx";
        }
        return "masked";
    }

    private String maskUserAgent(String userAgent) {
        if (userAgent == null) {
            return "unknown";
        }
        // Keep first 50 characters and mask the rest for privacy
        if (userAgent.length() > 50) {
            return userAgent.substring(0, 50) + "...";
        }
        return userAgent;
    }

}