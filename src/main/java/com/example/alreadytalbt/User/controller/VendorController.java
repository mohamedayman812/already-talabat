package com.example.alreadytalbt.User.controller;


import com.example.alreadytalbt.Order.dto.OrderResponseDTO;
import com.example.alreadytalbt.Order.dto.UpdateOrderDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemCreateDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemUpdateDTO;
import com.example.alreadytalbt.User.auth.JwtUtil;
import com.example.alreadytalbt.User.dto.*;
import com.example.alreadytalbt.User.model.Vendor;
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

    @Autowired
    private JwtUtil JwtUtils;

    // CREATE
    @PostMapping
    public ResponseEntity<?> createVendor(@RequestBody VendorCreateDTO dto,
                                                          @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        try {
            VendorResponseDTO vendor = vendorService.createVendor(dto,token);
            return new ResponseEntity<>(vendor, HttpStatus.CREATED);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


    // READ by ID
    @GetMapping
    public ResponseEntity<VendorResponseDTO> getVendor(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = JwtUtils.extractUserId(token);
        System.out.println("user id: "+userId);
        Vendor vendor = vendorService.getVendorByUserId(userId);
        System.out.println("vendor"+ vendor.toString());
        return ResponseEntity.ok(vendorService.getVendorById(vendor.getId()));
    }

    // READ all vendors
    @GetMapping("/all")
    public ResponseEntity<List<VendorResponseDTO>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<VendorResponseDTO> updateVendor(@RequestHeader("Authorization") String authHeader,
                                                          @RequestBody @Valid VendorUpdateDto dto) {

        String token = authHeader.replace("Bearer ", "");
        String userId = JwtUtils.extractUserId(token);
        Vendor vendor = vendorService.getVendorByUserId(userId);
        VendorResponseDTO updated = vendorService.updateVendor(vendor.getId(), dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping
    public ResponseEntity<Void> deleteVendor(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = JwtUtils.extractUserId(token);
        Vendor vendor = vendorService.getVendorByUserId(userId);
        vendorService.deleteVendor(vendor.getId());
        return ResponseEntity.noContent().build();
    }

    //ADD-MENUITEM
    @PostMapping("/vendorAdd/menuitems")
    public MenuItemDTO createMenuItem(@RequestHeader("Authorization") String authHeader, @RequestBody MenuItemCreateDTO dto) {
        String token = authHeader.replace("Bearer ", "");
        String userId = JwtUtils.extractUserId(token);
        Vendor vendor = vendorService.getVendorByUserId(userId);
        return vendorService.createMenuItem(vendor.getId(), dto);
    }

    //UPDATE-MENUITEM
    @PutMapping("/vendorUpdate/menuitems/{menuItemId}")
    public MenuItemDTO updateMenuItem( @RequestHeader("Authorization") String authHeader,@PathVariable String menuItemId, @RequestBody MenuItemUpdateDTO dto) {
        String token = authHeader.replace("Bearer ", "");
        String userId = JwtUtils.extractUserId(token);
        Vendor vendor = vendorService.getVendorByUserId(userId);
        return vendorService.updateMenuItem(menuItemId, dto,vendor.getId().toHexString());
    }

    //DELETE-MENUITEM
    @DeleteMapping("/vendorDelete/menuitems/{menuItemId}")
    public void deleteMenuItem(@PathVariable String menuItemId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = JwtUtils.extractUserId(token);
        Vendor vendor = vendorService.getVendorByUserId(userId);
        vendorService.deleteMenuItem(menuItemId, vendor.getId().toHexString());
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByVendorId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = JwtUtils.extractUserId(token);
        Vendor vendor = vendorService.getVendorByUserId(userId);
        List<OrderResponseDTO> orders = vendorService.getOrder(vendor.getId().toHexString());
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/status/{orderId}")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable String orderId,
                                                                @RequestHeader("Authorization") String authHeader)
    {
        String token = authHeader.replace("Bearer ", "");
        String userId = JwtUtils.extractUserId(token);
        Vendor vendor = vendorService.getVendorByUserId(userId);
        return ResponseEntity.ok(vendorService.updateOrderStatus(orderId,vendor.getId().toHexString(), token));
    }



}

