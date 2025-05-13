package com.example.alreadytalbt.User.FeignClient;
import com.example.alreadytalbt.Restaurant.dto.*;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "restaurant-service", url = "${restaurant.service.url}")
public interface RestaurantClient {

    @GetMapping("/api/restaurants/single/{id}")
    RestaurantResponseDTO getRestaurantById(@PathVariable("id") String id);

    @PostMapping("/api/restaurants/add")
    RestaurantResponseDTO createRestaurant(@RequestBody @Valid CreateRestaurantDTO restaurantDTO);

    @PutMapping("/api/restaurants/{id}")
    void updateRestaurants(@PathVariable("id") String id, @RequestBody RestaurantUpdateDto dto);


    @DeleteMapping("/api/restaurants/{id}")
    void deleteRestaurants(@PathVariable("id") String id);

    @GetMapping("/api/restaurants")
    List<RestaurantResponseDTO> getAllRestaurants();

    @PostMapping("/api/menu-items")
    MenuItemDTO createMenuItem(@RequestBody MenuItemCreateDTO dto);

    @PutMapping("/api/menu-items/{id}")
    MenuItemDTO updateMenuItems(@PathVariable String id, @RequestBody MenuItemUpdateDTO dto);

    @DeleteMapping("/api/menu-items/{id}")
    void deleteMenuItems(@PathVariable String id);

    @GetMapping("/api/menu-items/{id}")
    MenuItemDTO getMenuItemById(@PathVariable("id") String id);


}
