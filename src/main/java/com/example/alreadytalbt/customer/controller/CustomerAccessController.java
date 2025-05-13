package com.example.alreadytalbt.customer.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerAccessController {

    @GetMapping("/test-role")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String testCustomerAccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User: " + auth.getName());
        System.out.println("Authorities: " + auth.getAuthorities());
        return "✅ You have CUSTOMER access!";
    }
}
