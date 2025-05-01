package com.example.alreadytalbt.User.FeignClient;


import com.example.alreadytalbt.Restaurant.dto.CreateRestaurantDTO;
import com.example.alreadytalbt.Restaurant.dto.RestaurantResponseDTO;
import com.example.alreadytalbt.Restaurant.dto.RestaurantUpdateDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "restaurant-service", url = "${restaurant.service.url}")
public interface RestaurantClient {

    @GetMapping("/api/restaurants/single/{id}")
    RestaurantResponseDTO getRestaurantById(@PathVariable("id") String id);

    @PostMapping("/api/restaurants/add")
    RestaurantResponseDTO createRestaurant(@RequestBody @Valid CreateRestaurantDTO restaurantDTO);

    @PutMapping("/api/restaurants/{id}")
    void updateRestaurant(@PathVariable("id") String id, @RequestBody RestaurantUpdateDto dto);


    @DeleteMapping("/api/restaurants/{id}")
    void deleteRestaurant(@PathVariable("id") String id);


}
