package com.example.alreadytalbt.User.controller;


import com.example.alreadytalbt.Order.dto.OrderResponseDTO;
import com.example.alreadytalbt.Order.dto.UpdateOrderDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemCreateDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemUpdateDTO;
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
    @PostMapping("/{vendorId}/menuitems")
    public MenuItemDTO createMenuItem(@PathVariable String vendorId, @RequestBody MenuItemCreateDTO dto) {
        return vendorService.createMenuItem(new ObjectId(vendorId), dto);
    }

    //UPDATE-MENUITEM
    @PutMapping("/{vendorId}/menuitems/{menuItemId}")
    public MenuItemDTO updateMenuItem(@PathVariable String vendorId,@PathVariable String menuItemId, @RequestBody MenuItemUpdateDTO dto) {
        return vendorService.updateMenuItem(menuItemId, dto,vendorId);
    }

    //DELETE-MENUITEM
    @DeleteMapping("/{vendorId}/menuitems/{menuItemId}")
    public void deleteMenuItem(@PathVariable String menuItemId,@PathVariable String vendorId) {
        vendorService.deleteMenuItem(menuItemId,vendorId);
    }

    @GetMapping("/order/{vendorId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByVendorId(@PathVariable String vendorId) {
        List<OrderResponseDTO> orders = vendorService.getOrder(vendorId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{vendorId}/status/{orderId}")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable String orderId,@PathVariable String vendorId)
    {
        System.out.println("ven:"+vendorId);
        return ResponseEntity.ok(vendorService.updateOrderStatus(orderId,vendorId));
    }



}

