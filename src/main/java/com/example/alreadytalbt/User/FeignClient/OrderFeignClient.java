package com.example.alreadytalbt.User.FeignClient;

import com.example.alreadytalbt.Order.dto.OrderSummaryDTO;
import com.example.alreadytalbt.Order.dto.UpdateOrderDTO;
import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "order-service", url ="${order.service.url}")
public interface OrderFeignClient {

    @PutMapping("/api/orders/{orderId}/status")
    UpdateOrderDTO updateOrderStatus(@PathVariable("orderId") ObjectId orderId,
                            @RequestParam("status") String status);


//    @GetMapping("/by-delivery/{deliveryGuyId}")
//    List<Order> getOrdersByDeliveryGuy(@PathVariable("deliveryGuyId") ObjectId deliveryGuyId);

//    @PutMapping("/api/orders/assign-order/{orderId}/to-delivery/{deliveryGuyId}")
//    Order assignOrderToDelivery(@PathVariable("deliveryGuyId") String deliveryGuyId,
//            @PathVariable("orderId") String orderId
//            );



        @PutMapping("/api/orders/assign-order/{orderId}/to-delivery/{deliveryGuyId}")
        void assignOrderToDeliveryGuy(@PathVariable("orderId") ObjectId orderId,
                                      @PathVariable("deliveryGuyId") ObjectId deliveryGuyId);



    @GetMapping("/api/orders/summary")
    List<OrderSummaryDTO> getOrderSummaries();

    @GetMapping("/api/orders/customer/{customerId}")
    List<UpdateOrderDTO> getOrdersByCustomerId(@PathVariable("customerId") ObjectId customerId);
}


