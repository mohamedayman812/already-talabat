package com.example.alreadytalbt.Order.Controller;
import com.example.alreadytalbt.Order.Model.Order;
import com.example.alreadytalbt.Order.Service.OrderService;
import com.example.alreadytalbt.Order.dto.CreateOrderDTO;
import com.example.alreadytalbt.Order.dto.OrderResponseDTO;
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
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable String id) {
        try {
            OrderResponseDTO order = orderService.getById(new ObjectId(id));
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable String orderId,
                                                   @RequestParam String status) {
        try {
            OrderResponseDTO updatedOrder = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/summary")
    public List<OrderSummaryDTO> getOrderSummaries() {
        System.out.println("ana f controller");
        return orderService.getAllOrderSummaries(); // returns List<OrderSummaryDTO>
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable ObjectId customerId) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByRestaurantId(@PathVariable String restaurantId) {
        try {
            ObjectId id = new ObjectId(restaurantId);
            List<OrderResponseDTO> orders = orderService.getOrdersByRestaurantId(id);
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }



}
