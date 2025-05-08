package com.example.alreadytalbt.vendor.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor")
public class VendorAccessController {

    @GetMapping("/test-role")
    @PreAuthorize("hasRole('VENDOR')")
    public String testVendorAccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("👤 User: " + auth.getName());
        System.out.println("🎭 Authorities: " + auth.getAuthorities());
        return "✅ You have VENDOR access!";
    }
}
