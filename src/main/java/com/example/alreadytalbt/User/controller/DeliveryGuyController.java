package com.example.alreadytalbt.User.controller;

import com.example.alreadytalbt.Order.Model.Order;
import com.example.alreadytalbt.Order.dto.OrderResponseDTO;
import com.example.alreadytalbt.Order.dto.OrderSummaryDTO;
import com.example.alreadytalbt.Order.dto.UpdateOrderDTO;
import com.example.alreadytalbt.User.dto.CreateDeliveryGuyDTO;
import com.example.alreadytalbt.User.dto.UpdateDeliveryGuyDTO;
import com.example.alreadytalbt.User.model.DeliveryGuy;
import com.example.alreadytalbt.User.service.DeliveryGuyService;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<DeliveryGuy> register(@RequestBody CreateDeliveryGuyDTO dto) {
        return ResponseEntity.ok(deliveryGuyService.createDeliveryGuy(dto));
    }
    @PutMapping("/assign-order/{orderId}/to-delivery/{deliveryGuyId}")
    public ResponseEntity<Object> assignOrderToDeliveryGuy(@PathVariable ObjectId orderId, @PathVariable ObjectId deliveryGuyId) {

        deliveryGuyService.assignOrderToDeliveryGuy( orderId, deliveryGuyId);
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
    public ResponseEntity<OrderResponseDTO> updateStatus(@PathVariable String orderId, @RequestParam String status)
    {
        return ResponseEntity.ok(deliveryGuyService.updateOrderStatus(orderId, status));
    }


    @GetMapping("/{id}")
    public ResponseEntity<UpdateDeliveryGuyDTO> getDeliveryGuyById(@PathVariable ObjectId id) {
        UpdateDeliveryGuyDTO deliveryGuy = deliveryGuyService.getDeliveryGuyById(id);

        if (deliveryGuy == null) {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }

        return ResponseEntity.ok(deliveryGuy); // Return 200 OK with DeliveryGuy
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateDeliveryGuyDTO> updateDeliveryGuy(
            @PathVariable ObjectId id,
            @Valid @RequestBody UpdateDeliveryGuyDTO dto) {

        try {
            UpdateDeliveryGuyDTO updatedDeliveryGuy = deliveryGuyService.updateDeliveryGuy(id, dto);
            return ResponseEntity.ok(updatedDeliveryGuy);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/summary")
    public ResponseEntity<List<OrderSummaryDTO>> getAllOrderSummaries() {
        return ResponseEntity.ok(deliveryGuyService.getAllOrdersForDeliveryGuy());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDeliveryGuy(@PathVariable ObjectId id) {
        boolean deleted = deliveryGuyService.deleteDeliveryGuy(id);
        if (deleted) {
            return ResponseEntity.ok("Delivery guy deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delivery guy not found.");
        }
    }


}

