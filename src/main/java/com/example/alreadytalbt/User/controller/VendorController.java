package com.example.alreadytalbt.User.controller;


import com.example.alreadytalbt.Order.dto.OrderResponseDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemCreateDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemUpdateDTO;
import com.example.alreadytalbt.User.auth.JwtUtil;
import com.example.alreadytalbt.User.auth.RequireAuthentication;
import com.example.alreadytalbt.User.dto.*;
import com.example.alreadytalbt.User.model.Vendor;
import com.example.alreadytalbt.User.service.VendorService;
import jakarta.servlet.http.HttpServletRequest;
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
    @RequireAuthentication
    public ResponseEntity<?> createVendor(@RequestBody VendorCreateDTO dto,
                                          HttpServletRequest request) {

        String userId = (String) request.getAttribute("userId");
        try {
            VendorResponseDTO vendor = vendorService.createVendor(dto,userId);
            return new ResponseEntity<>(vendor, HttpStatus.CREATED);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


    // READ by ID
    @GetMapping
    @RequireAuthentication
    public ResponseEntity<VendorResponseDTO> getVendor(HttpServletRequest request) {
//        //String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        Vendor vendor = vendorService.getVendorByUserId(userId);
        return ResponseEntity.ok(vendorService.getVendorById(vendor.getId()));
    }

    // READ all vendors
    @GetMapping("/all")
    public ResponseEntity<List<VendorResponseDTO>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    // UPDATE
    @PutMapping
    @RequireAuthentication
    public ResponseEntity<VendorResponseDTO> updateVendor(HttpServletRequest request,
                                                          @RequestBody @Valid VendorUpdateDto dto) {

        //String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        Vendor vendor = vendorService.getVendorByUserId(userId);
        VendorResponseDTO updated = vendorService.updateVendor(vendor.getId(), dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping
    @RequireAuthentication
    public ResponseEntity<Void> deleteVendor(HttpServletRequest request) {
        //String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        Vendor vendor = vendorService.getVendorByUserId(userId);
        vendorService.deleteVendor(vendor.getId());
        return ResponseEntity.noContent().build();
    }

    //ADD-MENUITEM
    @PostMapping("/vendorAdd/menuitems")
    @RequireAuthentication
    public MenuItemDTO createMenuItem(HttpServletRequest request, @RequestBody MenuItemCreateDTO dto) {
        //String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        Vendor vendor = vendorService.getVendorByUserId(userId);
        return vendorService.createMenuItem(vendor.getId(), dto);
    }

    //UPDATE-MENUITEM
    @PutMapping("/vendorUpdate/menuitems/{menuItemId}")
    @RequireAuthentication
    public MenuItemDTO updateMenuItem( HttpServletRequest request,@PathVariable String menuItemId, @RequestBody MenuItemUpdateDTO dto) {
        //String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        Vendor vendor = vendorService.getVendorByUserId(userId);
        return vendorService.updateMenuItem(menuItemId, dto,vendor.getId().toHexString());
    }

    //DELETE-MENUITEM
    @DeleteMapping("/vendorDelete/menuitems/{menuItemId}")
    @RequireAuthentication
    public void deleteMenuItem(@PathVariable String menuItemId, HttpServletRequest request) {
        //String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        Vendor vendor = vendorService.getVendorByUserId(userId);
        vendorService.deleteMenuItem(menuItemId, vendor.getId().toHexString());
    }

    @GetMapping("/orders")
    @RequireAuthentication
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByVendorId(HttpServletRequest request) {
        //String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        Vendor vendor = vendorService.getVendorByUserId(userId);
        List<OrderResponseDTO> orders = vendorService.getOrder(vendor.getId().toHexString());
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/status/{orderId}")
    @RequireAuthentication
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable String orderId,
                                                              HttpServletRequest request)
    {
        //String token = authHeader.replace("Bearer ", "");
        String userId = (String) request.getAttribute("userId");
        Vendor vendor = vendorService.getVendorByUserId(userId);
        return ResponseEntity.ok(vendorService.updateOrderStatus(orderId,vendor.getId().toHexString()));
    }



}

