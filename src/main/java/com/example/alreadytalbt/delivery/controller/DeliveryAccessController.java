package com.example.alreadytalbt.delivery.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryAccessController {

    @GetMapping("/test-role")
    @PreAuthorize("hasRole('DELIVERY')")
    public String testDELIVERYAccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("👤 User: " + auth.getName());
        System.out.println("🎭 Authorities: " + auth.getAuthorities());
        return "✅ You have DELIVERY access!";
    }
}