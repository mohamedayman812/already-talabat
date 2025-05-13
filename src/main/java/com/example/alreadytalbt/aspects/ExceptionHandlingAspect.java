package com.example.alreadytalbt.aspects;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlingAspect {

    // Match any method in your base package, excluding security filters
    @AfterThrowing(
            pointcut = "execution(* com.example.alreadytalbt..*(..)) && !within(com.example.alreadytalbt.User.auth.security..*)",
            throwing = "ex")
    public void logRuntimeExceptions(RuntimeException ex) {
        System.err.println("AspectEH:❗ Exception caught in aspect: " + ex.getMessage());
    }
}
