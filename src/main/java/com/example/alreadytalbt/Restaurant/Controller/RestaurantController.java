package com.example.alreadytalbt.Restaurant.Controller;

import com.example.alreadytalbt.Restaurant.Model.MenuItem;
import com.example.alreadytalbt.Restaurant.Model.Restaurant;
import com.example.alreadytalbt.Restaurant.Service.RestaurantService;
import com.example.alreadytalbt.Restaurant.dto.RestaurantDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/add")
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody @Valid RestaurantDTO restaurantDTO) {
        return ResponseEntity.ok(restaurantService.addRestaurant(restaurantDTO));
    }


    @GetMapping("/{id}")
    public ResponseEntity<List<MenuItem>> getMenu(@PathVariable String id) {
        return ResponseEntity.ok(restaurantService.getMenu(id));
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAll() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(
            @PathVariable String id,
            @RequestBody @Valid RestaurantDTO restaurantDTO) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, restaurantDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable String id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

}
