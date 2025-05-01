package com.example.alreadytalbt.Order.Controller;
import com.example.alreadytalbt.Order.Model.Order;
import com.example.alreadytalbt.Order.Service.OrderService;
import com.example.alreadytalbt.Order.dto.CreateOrderDTO;
import com.example.alreadytalbt.Order.dto.OrderSummaryDTO;
import com.example.alreadytalbt.Order.dto.UpdateOrderDTO;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable ObjectId id) {
        Optional<Order> orderOptional = orderService.getById(id);
        return orderOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderDTO orderDTO) {
        Order newOrder = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable ObjectId id, @Valid @RequestBody UpdateOrderDTO orderDTO) {
        try {
            Order updatedOrder = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(updatedOrder);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable ObjectId id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/assign-order/{orderId}/to-delivery/{deliveryGuyId}")
    public ResponseEntity<Object> assignOrderToDeliveryGuy(@PathVariable ObjectId orderId, @PathVariable ObjectId deliveryGuyId) {

        orderService.assignOrderToDelivery( orderId,deliveryGuyId);
        return ResponseEntity.ok().build();
    }




    @GetMapping("/by-delivery/{deliveryGuyId}")
    public ResponseEntity<List<Order>> getOrdersByDeliveryGuy(@PathVariable ObjectId deliveryGuyId) {
        return ResponseEntity.ok(orderService.getOrdersByDeliveryGuy(deliveryGuyId));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<UpdateOrderDTO> updateOrderStatus(@PathVariable ObjectId orderId,
                                                   @RequestParam String status) {
        try {
            UpdateOrderDTO updatedOrder = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/summary")
    public List<OrderSummaryDTO> getOrderSummaries() {
        return orderService.getAllOrderSummaries(); // returns List<OrderSummaryDTO>
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable ObjectId customerId) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

}
