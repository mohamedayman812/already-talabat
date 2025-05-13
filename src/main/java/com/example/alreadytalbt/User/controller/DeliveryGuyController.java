package com.example.alreadytalbt.User.controller;

import com.example.alreadytalbt.Order.dto.OrderResponseDTO;
import com.example.alreadytalbt.Order.dto.OrderSummaryDTO;
import com.example.alreadytalbt.User.auth.RequireAuthentication;
import com.example.alreadytalbt.User.dto.*;
import com.example.alreadytalbt.User.model.DeliveryGuy;
import com.example.alreadytalbt.User.service.DeliveryGuyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.aspectj.weaver.patterns.IToken;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryGuyController {

    @Autowired
    private DeliveryGuyService deliveryGuyService;
    // CREATE
    @PostMapping
    @RequireAuthentication
    public ResponseEntity<?> register(@RequestBody CreateDeliveryGuyDTO dto,
                                                HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        try {
            return ResponseEntity.ok(deliveryGuyService.createDeliveryGuy(dto, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/assign-order/{orderId}/to-deliveryGuy")
    @RequireAuthentication
    public ResponseEntity<Object> assignOrderToDeliveryGuy(@PathVariable ObjectId orderId,
                                                           HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        deliveryGuyService.assignOrderToDeliveryGuy( orderId, userId);
        return ResponseEntity.ok().build();
    }



    @PutMapping("/{orderId}/status")
    @RequireAuthentication
    public ResponseEntity<OrderResponseDTO> updateStatus(@PathVariable String orderId,
                                                         @RequestParam String status, HttpServletRequest request)
    {

        return ResponseEntity.ok(deliveryGuyService.updateOrderStatus(orderId, status));
    }


    @GetMapping
    @RequireAuthentication
    public ResponseEntity<UpdateDeliveryGuyDTO> getDeliveryGuyById( HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        UpdateDeliveryGuyDTO deliveryGuy = deliveryGuyService.getDeliveryGuyById(userId);

        if (deliveryGuy == null) {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }

        return ResponseEntity.ok(deliveryGuy); // Return 200 OK with DeliveryGuy
    }


    @PutMapping("/update")
    @RequireAuthentication
    public ResponseEntity<UpdateDeliveryGuyDTO> updateDeliveryGuy(
            @Valid @RequestBody UpdateDeliveryGuyDTO dto, HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute("userId");
            UpdateDeliveryGuyDTO updatedDeliveryGuy = deliveryGuyService.updateDeliveryGuy(dto, userId);
            return ResponseEntity.ok(updatedDeliveryGuy);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/summary")
    @RequireAuthentication
    public ResponseEntity<List<OrderSummaryDTO>> getAllOrderSummaries( HttpServletRequest request) {
        return ResponseEntity.ok(deliveryGuyService.getAllOrdersForDeliveryGuy());
    }

    @DeleteMapping("/delete")
    @RequireAuthentication
    public ResponseEntity<String> deleteDeliveryGuy(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        boolean deleted = deliveryGuyService.deleteDeliveryGuy(userId);
        if (deleted) {
            return ResponseEntity.ok("Delivery guy deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delivery guy not found.");
        }
    }


}

