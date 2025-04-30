package com.example.alreadytalbt.User.FeignClient;

import com.example.alreadytalbt.Order.Model.Order;
import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "order-service", url ="${order.service.url}")
public interface OrderFeignClient {

    @PutMapping("/api/orders/{orderId}/status")
    Order updateOrderStatus(@PathVariable("orderId") ObjectId orderId,
                            @RequestParam("status") String status);


//    @GetMapping("/by-delivery/{deliveryGuyId}")
//    List<Order> getOrdersByDeliveryGuy(@PathVariable("deliveryGuyId") ObjectId deliveryGuyId);

//    @PutMapping("/api/orders/assign-order/{orderId}/to-delivery/{deliveryGuyId}")
//    Order assignOrderToDelivery(@PathVariable("deliveryGuyId") String deliveryGuyId,
//            @PathVariable("orderId") String orderId
//            );



}


