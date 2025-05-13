package com.example.alreadytalbt.aspects;



import com.example.alreadytalbt.User.auth.JwtUtil;
import com.example.alreadytalbt.User.auth.security.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthAspect {

    @Autowired
    private  JwtUtil jwtService;

    @Autowired
    private  UserDetailsServiceImpl userDetailsService;

    @Pointcut("@annotation(com.example.alreadytalbt.User.auth.RequireAuthentication)")
    public void requireAuthAnnotation() {}

    @Before("requireAuthAnnotation()")
    public void authenticate(JoinPoint joinPoint) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) throw new RuntimeException("No request attributes found");

        HttpServletRequest request = attributes.getRequest();
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.replace("Bearer ", "");
        String userId = jwtService.extractUserId(token);
        System.out.println("user IDd: "+userId);
        // Store userId in request attribute for use in controller
        request.setAttribute("userId", userId);
    }
}
