package com.example.alreadytalbt.User.controller;


import com.example.alreadytalbt.User.dto.VendorCreateDTO;
import com.example.alreadytalbt.User.dto.VendorResponseDTO;
import com.example.alreadytalbt.User.dto.VendorUpdateDto;
import com.example.alreadytalbt.User.service.VendorService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<VendorResponseDTO> createVendor(@RequestBody @Valid VendorCreateDTO dto) {
        VendorResponseDTO vendor = vendorService.createVendor(dto);
        return new ResponseEntity<>(vendor, HttpStatus.CREATED);
    }

    // READ by ID
    @GetMapping("/{id}")
    public ResponseEntity<VendorResponseDTO> getVendor(@PathVariable ObjectId id) {
        return ResponseEntity.ok(vendorService.getVendorById(id));
    }

    // READ all vendors
    @GetMapping
    public ResponseEntity<List<VendorResponseDTO>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<VendorResponseDTO> updateVendor(@PathVariable ObjectId id, @RequestBody @Valid VendorUpdateDto dto) {
        VendorResponseDTO updated = vendorService.updateVendor(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable ObjectId id) {
        vendorService.deleteVendor(id);
        return ResponseEntity.noContent().build();
    }

    //ADD-MENUITEM

}

