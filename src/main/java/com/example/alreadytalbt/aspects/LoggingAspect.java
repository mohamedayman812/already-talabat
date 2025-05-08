package com.example.alreadytalbt.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Match any method inside any class annotated with @Service
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceMethods() {}

    // Match any method inside any class annotated with @RestController
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {}

    // ==================== SERVICE LOGGING ====================

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();

        StringBuilder logMessage = new StringBuilder("➡Called: ").append(methodName).append(" with args: [");
        for (int i = 0; i < args.length; i++) {
            logMessage.append(paramNames[i]).append("=").append(args[i]);
            if (i < args.length - 1) logMessage.append(", ");
        }
        logMessage.append("]");

        logger.info(logMessage.toString());
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().toShortString();
        logger.info(" Returned from: {} with result: {}", methodName, result);
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logError(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().toShortString();
        logger.error("Exception in {}: {}", methodName, ex.getMessage(), ex);
    }

    // ==================== CONTROLLER LOGGING ====================

    @Before("controllerMethods()")
    public void logBeforeController(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();

        StringBuilder logMessage = new StringBuilder("[API Call] -> ").append(methodName).append(" with args: [");
        for (int i = 0; i < args.length; i++) {
            logMessage.append(paramNames[i]).append("=").append(args[i]);
            if (i < args.length - 1) logMessage.append(", ");
        }
        logMessage.append("]");

        logger.info(logMessage.toString());
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterController(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().toShortString();
        logger.info("[API Response] <- {} with result: {}", methodName, result);
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public void logControllerError(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().toShortString();
        logger.error(" [API Error] in {}: {}", methodName, ex.getMessage(), ex);
    }
}
