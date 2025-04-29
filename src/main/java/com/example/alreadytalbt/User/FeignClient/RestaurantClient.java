package com.example.alreadytalbt.User.FeignClient;


import com.example.alreadytalbt.Restaurant.dto.RestaurantDTO;
import com.example.alreadytalbt.Restaurant.dto.RestaurantUpdateDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "restaurant-service", url = "${restaurant.service.url}")
public interface RestaurantClient {

    @GetMapping("/api/restaurants/{id}")
    RestaurantDTO getRestaurantById(@PathVariable("id") String id);

    @PostMapping("/api/restaurants/add")
    RestaurantDTO createRestaurant(@RequestBody @Valid RestaurantDTO restaurantDTO);

    @PutMapping("/api/restaurants/{id}")
    void updateRestaurant(@PathVariable("id") String id, @RequestBody RestaurantUpdateDto dto);



    @DeleteMapping("/api/restaurants/{id}")
    void deleteRestaurant(@PathVariable("id") String id);


}
