package com.alrs.loan_recovery_system.config.logging;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {
    
    private final ObjectMapper objectMapper;

    // Pointcut for all public methods in service layer
    @Pointcut("execution(public * com.alrs.loan_recovery_system..service..*(..))")
    public void serviceLayer() {}

    // Pointcut for all public methods in controller layer
    @Pointcut("execution(public * com.alrs.loan_recovery_system..controller..*(..))")
    public void controllerLayer() {}

    // Pointcut for all public methods in repository layer
//    @Pointcut("execution(public * com.alrs.loan_recovery_system..repository..*(..))")
//    public void repositoryLayer() {}

    // Combined pointcut for all layers
    @Pointcut("serviceLayer() || controllerLayer()")
    public void applicationLayers() {}

    @Around("applicationLayers()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String fullMethodName = className + "." + methodName;

        // Log method entry
        Object[] args = joinPoint.getArgs();
        String maskedArgs = maskSensitiveData(args);

        log.info("ENTER -> {}: args={}", fullMethodName, maskedArgs);

        stopWatch.start();
        Object result = null;

        try {
            result = joinPoint.proceed();
            stopWatch.stop();

            // Log successful method exit
            String maskedResult = maskSensitiveData(result);
            log.info("EXIT -> {} completed in {}ms: return={}",
                    fullMethodName, stopWatch.getTotalTimeMillis(), maskedResult);

            return result;

        } catch (Exception ex) {
            stopWatch.stop();

            // Log method exit with exception
            log.error("EXCEPTION -> {} failed in {}ms: exception={}",
                    fullMethodName, stopWatch.getTotalTimeMillis(), ex.getMessage(), ex);

            throw ex;
        }
    }

//    @AfterThrowing(pointcut = "applicationLayers()", throwing = "exception")
//    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
//        String className = joinPoint.getTarget().getClass().getSimpleName();
//        String methodName = joinPoint.getSignature().getName();
//
//        log.error("EXCEPTION in {}.{}: {}", className, methodName, exception.getMessage(), exception);
//    }

    // Specific logging for performance-critical methods
    @Around("@annotation(com.alrs.loan_recovery_system.logging.annotation.LogPerformance)")
    public Object logPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        stopWatch.start();

        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();

            long executionTime = stopWatch.getTotalTimeMillis();

            // Log to audit logger for performance monitoring
            org.slf4j.LoggerFactory.getLogger("AUDIT").info(
                    "PERFORMANCE: {}.{} executed in {}ms",
                    className, methodName, executionTime
            );

            // Log warning if method takes too long
            if (executionTime > 1000) {
                log.warn("SLOW_EXECUTION: {}.{} took {}ms", className, methodName, executionTime);
            }

            return result;

        } catch (Exception ex) {
            stopWatch.stop();
            log.error("PERFORMANCE_ERROR: {}.{} failed after {}ms",
                    className, methodName, stopWatch.getTotalTimeMillis(), ex);
            throw ex;
        }
    }

    private String maskSensitiveData(Object data) {
        if (data == null) {
            return "null";
        }

        try {
            String jsonString = objectMapper.writeValueAsString(data);

            // Mask sensitive fields
            jsonString = jsonString.replaceAll("(\"password\"\\s*:\\s*\")[^\"]*\"", "$1***\"");
            jsonString = jsonString.replaceAll("(\"token\"\\s*:\\s*\")[^\"]*\"", "$1***\"");
            jsonString = jsonString.replaceAll("(\"secret\"\\s*:\\s*\")[^\"]*\"", "$1***\"");
            jsonString = jsonString.replaceAll("(\"key\"\\s*:\\s*\")[^\"]*\"", "$1***\"");
            jsonString = jsonString.replaceAll("(\"authorization\"\\s*:\\s*\")[^\"]*\"", "$1***\"");
            jsonString = jsonString.replaceAll("(\"ssn\"\\s*:\\s*\")[^\"]*\"", "$1***\"");
            jsonString = jsonString.replaceAll("(\"creditCard\"\\s*:\\s*\")[^\"]*\"", "$1***\"");
//            jsonString = jsonString.replaceAll("(\"email\"\\s*:\\s*\")([^\"@]*)[^\"]*\"", "$1$2***\"");

            // Limit length to prevent log explosion
            if (jsonString.length() > 500) {
                jsonString = jsonString.substring(0, 497) + "...";
            }

            return jsonString;

        } catch (JsonProcessingException e) {
            // Fallback for objects that can't be serialized
            String toString = data.toString();
            if (toString.length() > 200) {
                toString = toString.substring(0, 197) + "...";
            }
            return toString;
        }
    }

    private String getMethodSignature(JoinPoint joinPoint) {
        Method method = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getMethod();
        return method.getDeclaringClass().getSimpleName() + "." + method.getName() +
                "(" + Arrays.toString(method.getParameterTypes()) + ")";
    }
}