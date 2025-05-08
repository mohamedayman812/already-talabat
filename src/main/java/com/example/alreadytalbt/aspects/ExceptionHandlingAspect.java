package com.example.alreadytalbt.aspects;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;




@Aspect
@Component
public class ExceptionHandlingAspect {

    @AfterThrowing(pointcut = "execution(* com.yourpackage..*(..))", throwing = "ex")
    public void logRuntimeExceptions(RuntimeException ex) {
        System.err.println("❗ Exception caught in aspect: " + ex.getMessage());
        // Optional: send to logger, monitoring service, etc.
    }
}

