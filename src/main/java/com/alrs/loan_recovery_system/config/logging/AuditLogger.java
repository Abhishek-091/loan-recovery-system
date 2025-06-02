package com.alrs.loan_recovery_system.config.logging;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AuditLogger {

    private static final Logger AUDIT_LOGGER = LoggerFactory.getLogger("AUDIT");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void logUserAction(String action, String userId, String resource) {
        logUserAction(action, userId, resource, null, null);
    }

    public void logUserAction(String action, String userId, String resource,
                              String details, Map<String, Object> additionalData) {

        Map<String, Object> auditData = new HashMap<>();
        auditData.put("timestamp", LocalDateTime.now().format(FORMATTER));
        auditData.put("action", action);
        auditData.put("userId", maskUserId(userId));
        auditData.put("resource", resource);
        auditData.put("traceId", MDC.get("traceId"));
        auditData.put("sessionId", MDC.get("sessionId"));
        auditData.put("clientIp", MDC.get("clientIp"));

        if (details != null) {
            auditData.put("details", details);
        }

        if (additionalData != null) {
            auditData.putAll(additionalData);
        }

        AUDIT_LOGGER.info("USER_ACTION: {}", auditData);
    }

    public void logSecurityEvent(String eventType, String severity, String description) {
        logSecurityEvent(eventType, severity, description, null);
    }

    public void logSecurityEvent(String eventType, String severity, String description,
                                 Map<String, Object> context) {

        Map<String, Object> securityData = new HashMap<>();
        securityData.put("timestamp", LocalDateTime.now().format(FORMATTER));
        securityData.put("eventType", eventType);
        securityData.put("severity", severity);
        securityData.put("description", description);
        securityData.put("traceId", MDC.get("traceId"));
        securityData.put("clientIp", MDC.get("clientIp"));
        securityData.put("userAgent", MDC.get("userAgent"));

        if (context != null) {
            securityData.putAll(context);
        }

        AUDIT_LOGGER.warn("SECURITY_EVENT: {}", securityData);
    }

    public void logDataAccess(String operation, String tableName, String userId,
                              int recordCount) {
        logDataAccess(operation, tableName, userId, recordCount, null);
    }

    public void logDataAccess(String operation, String tableName, String userId,
                              int recordCount, String criteria) {

        Map<String, Object> dataAccessLog = new HashMap<>();
        dataAccessLog.put("timestamp", LocalDateTime.now().format(FORMATTER));
        dataAccessLog.put("operation", operation);
        dataAccessLog.put("tableName", tableName);
        dataAccessLog.put("userId", maskUserId(userId));
        dataAccessLog.put("recordCount", recordCount);
        dataAccessLog.put("traceId", MDC.get("traceId"));

        if (criteria != null) {
            dataAccessLog.put("criteria", criteria);
        }

        AUDIT_LOGGER.info("DATA_ACCESS: {}", dataAccessLog);
    }

    public void logBusinessEvent(String eventName, String entityType, String entityId,
                                 String userId, Map<String, Object> eventData) {

        Map<String, Object> businessEvent = new HashMap<>();
        businessEvent.put("timestamp", LocalDateTime.now().format(FORMATTER));
        businessEvent.put("eventName", eventName);
        businessEvent.put("entityType", entityType);
        businessEvent.put("entityId", entityId);
        businessEvent.put("userId", maskUserId(userId));
        businessEvent.put("traceId", MDC.get("traceId"));

        if (eventData != null) {
            businessEvent.putAll(eventData);
        }

        AUDIT_LOGGER.info("BUSINESS_EVENT: {}", businessEvent);
    }

    public void logApiCall(String endpoint, String method, int statusCode,
                           long responseTime, String userId) {

        Map<String, Object> apiCallLog = new HashMap<>();
        apiCallLog.put("timestamp", LocalDateTime.now().format(FORMATTER));
        apiCallLog.put("endpoint", endpoint);
        apiCallLog.put("method", method);
        apiCallLog.put("statusCode", statusCode);
        apiCallLog.put("responseTime", responseTime);
        apiCallLog.put("userId", maskUserId(userId));
        apiCallLog.put("traceId", MDC.get("traceId"));
        apiCallLog.put("clientIp", MDC.get("clientIp"));

        AUDIT_LOGGER.info("API_CALL: {}", apiCallLog);
    }

    private String maskUserId(String userId) {
        if (userId == null || userId.length() <= 4) {
            return "***";
        }
        return userId.substring(0, 2) + "***" + userId.substring(userId.length() - 2);
    }
}