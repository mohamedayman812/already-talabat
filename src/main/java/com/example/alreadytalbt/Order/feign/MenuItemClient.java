package com.example.alreadytalbt.Order.feign;

import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "menu-service", url = "${restaurant.service.url}")
public interface MenuItemClient {

    @GetMapping("/api/menu/{id}")
    MenuItemDTO getMenuItemById(@PathVariable("id") String id);

    @GetMapping("/api/menu")
    List<MenuItemDTO> getAllMenuItems();

    @PostMapping("/api/menu")
    MenuItemDTO createMenuItem(@RequestBody MenuItemDTO item);

    @PutMapping("/api/menu/{id}")
    MenuItemDTO updateMenuItem(@PathVariable("id") String id, @RequestBody MenuItemDTO item);

    @DeleteMapping("/api/menu/{id}")
    void deleteMenuItem(@PathVariable("id") String id);
}
