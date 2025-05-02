package com.example.alreadytalbt.Order.feign;


import com.example.alreadytalbt.Restaurant.dto.*;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "restaurant-service-order", url = "${restaurant.service.url}")
public interface RestrauntClient {


    @GetMapping("/api/menu-items/{menuItemId}")
    MenuItemDTO getMenuItemById(@PathVariable("menuItemId") String menuItemId);

}
