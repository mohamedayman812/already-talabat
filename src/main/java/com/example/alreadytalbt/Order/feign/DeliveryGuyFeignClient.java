package com.example.alreadytalbt.Order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "deliveryguy-service", url = "http://localhost:8080")
public interface DeliveryGuyFeignClient {
    @PutMapping("/api/delivery/assign-order/{orderId}/to-delivery/{deliveryGuyId}")
    void assignOrderToDeliveryGuy(@PathVariable("orderId") String orderId,
                                  @PathVariable("deliveryGuyId") String deliveryGuyId);
}

