package com.example.alreadytalbt.User.controller;


import com.example.alreadytalbt.User.dto.VendorDTO;
import com.example.alreadytalbt.User.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    // CREATE
    @PostMapping
    public ResponseEntity<VendorDTO> createVendor(@RequestBody VendorDTO dto) {
        return ResponseEntity.ok(vendorService.createVendor(dto));
    }

    // READ by ID
    @GetMapping("/{id}")
    public ResponseEntity<VendorDTO> getVendor(@PathVariable String id) {
        return ResponseEntity.ok(vendorService.getVendorById(id));
    }

    // READ all vendors
    @GetMapping
    public ResponseEntity<List<VendorDTO>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<VendorDTO> updateVendor(@PathVariable String id, @RequestBody VendorDTO dto) {
        return ResponseEntity.ok(vendorService.updateVendor(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable String id) {
        vendorService.deleteVendor(id);
        return ResponseEntity.noContent().build();
    }
}

