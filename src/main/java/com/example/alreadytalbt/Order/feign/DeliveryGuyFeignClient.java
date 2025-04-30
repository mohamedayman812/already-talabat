package com.example.alreadytalbt.Order.feign;

import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "user-service", url = "http://localhost:8080")
public interface DeliveryGuyFeignClient {
    @PutMapping("/api/delivery/assign-order/{orderId}/to-delivery/{deliveryGuyId}")
    void assignOrderToDeliveryGuy(@PathVariable("orderId") ObjectId orderId,
                                  @PathVariable("deliveryGuyId") ObjectId deliveryGuyId);
}

