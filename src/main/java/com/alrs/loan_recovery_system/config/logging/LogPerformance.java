package com.alrs.loan_recovery_system.config.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods for performance logging.
 * Methods annotated with @LogPerformance will have their execution time logged
 * and will generate warnings if they exceed performance thresholds.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogPerformance {

    /**
     * Threshold in milliseconds for slow execution warning
     */
    long threshold() default 1000L;

    /**
     * Whether to log method arguments
     */
    boolean logArgs() default false;

    /**
     * Whether to log return value
     */
    boolean logReturn() default false;
}
