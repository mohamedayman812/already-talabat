package com.example.alreadytalbt.User.controller;

import com.example.alreadytalbt.Order.dto.OrderResponseDTO;
import com.example.alreadytalbt.Order.dto.OrderSummaryDTO;
import com.example.alreadytalbt.User.dto.*;
import com.example.alreadytalbt.User.model.DeliveryGuy;
import com.example.alreadytalbt.User.service.DeliveryGuyService;
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
    public ResponseEntity<?> register(@RequestBody CreateDeliveryGuyDTO dto,
                                                @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        try {
            return ResponseEntity.ok(deliveryGuyService.createDeliveryGuy(dto, token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/assign-order/{orderId}/to-deliveryGuy")
    public ResponseEntity<Object> assignOrderToDeliveryGuy(@PathVariable ObjectId orderId,
                                                           @RequestHeader("Authorization") String authHeader) {

        System.out.println("fel controller");
        String token = authHeader.replace("Bearer ", "");
        deliveryGuyService.assignOrderToDeliveryGuy( orderId, token);
        return ResponseEntity.ok().build();
    }


//    @PutMapping("delivery/assign-order/{orderId}/to-delivery/{deliveryGuyId}")
//    public ResponseEntity<Order> assignOrderToDeliveryGuy(@PathVariable ObjectId orderId,
//                                                          @PathVariable ObjectId deliveryGuyId) {
//        System.out.println("AAAAAAAA");
//        // Call the service to assign the delivery guy and update the order's status
//        Order updatedOrder = deliveryGuyService.assignOrderToDelivery(orderId, deliveryGuyId);
//        return ResponseEntity.ok(updatedOrder);  // Return the updated order
//    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(@PathVariable String orderId,
                                                         @RequestParam String status, @RequestHeader("Authorization") String authHeader)
    {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(deliveryGuyService.updateOrderStatus(orderId, status, token));
    }


    @GetMapping
    public ResponseEntity<UpdateDeliveryGuyDTO> getDeliveryGuyById( @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UpdateDeliveryGuyDTO deliveryGuy = deliveryGuyService.getDeliveryGuyById(token);

        if (deliveryGuy == null) {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }

        return ResponseEntity.ok(deliveryGuy); // Return 200 OK with DeliveryGuy
    }


    @PutMapping("/update")
    public ResponseEntity<UpdateDeliveryGuyDTO> updateDeliveryGuy(
            @Valid @RequestBody UpdateDeliveryGuyDTO dto, @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");
            UpdateDeliveryGuyDTO updatedDeliveryGuy = deliveryGuyService.updateDeliveryGuy(dto, token);
            return ResponseEntity.ok(updatedDeliveryGuy);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/summary")
    public ResponseEntity<List<OrderSummaryDTO>> getAllOrderSummaries( @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(deliveryGuyService.getAllOrdersForDeliveryGuy(token));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDeliveryGuy(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        boolean deleted = deliveryGuyService.deleteDeliveryGuy(token);
        if (deleted) {
            return ResponseEntity.ok("Delivery guy deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delivery guy not found.");
        }
    }


}

