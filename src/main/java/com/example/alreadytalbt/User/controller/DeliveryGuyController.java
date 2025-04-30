package com.example.alreadytalbt.User.controller;

import com.example.alreadytalbt.Order.Model.Order;
import com.example.alreadytalbt.Order.dto.UpdateOrderStatusDTO;
import com.example.alreadytalbt.User.dto.CreateDeliveryGuyDTO;
import com.example.alreadytalbt.User.dto.UpdateDeliveryGuyDTO;
import com.example.alreadytalbt.User.model.DeliveryGuy;
import com.example.alreadytalbt.User.service.DeliveryGuyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryGuyController {

    @Autowired
    private DeliveryGuyService deliveryGuyService;

    @PostMapping
    public ResponseEntity<DeliveryGuy> register(@RequestBody CreateDeliveryGuyDTO dto) {
        return ResponseEntity.ok(deliveryGuyService.createDeliveryGuy(dto));
    }



    @PutMapping("/update-order-status")
    public ResponseEntity<Order> updateOrderStatus(@RequestBody UpdateOrderStatusDTO dto) {
        return ResponseEntity.ok(deliveryGuyService.updateOrderStatus(dto.getOrderId(), dto.getNewStatus()));
    }


    @GetMapping
    public ResponseEntity<List<DeliveryGuy>> getAll() {
        return ResponseEntity.ok(deliveryGuyService.getAll());
    }


    @PutMapping("/{id}")
    public ResponseEntity<UpdateDeliveryGuyDTO> updateDeliveryGuy(@PathVariable String id, @Valid @RequestBody UpdateDeliveryGuyDTO dto) {

        return deliveryGuyService.updateDeliveryGuy(id, dto)
                .map(updateddeliveryGuy -> new ResponseEntity<>(updateddeliveryGuy, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDeliveryGuy(@PathVariable String id) {
        boolean deleted = deliveryGuyService.deleteDeliveryGuy(id);
        if (deleted) {
            return ResponseEntity.ok("Delivery guy deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delivery guy not found.");
        }
    }
}

