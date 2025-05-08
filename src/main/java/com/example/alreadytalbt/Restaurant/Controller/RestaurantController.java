package com.example.alreadytalbt.Restaurant.Controller;

import com.example.alreadytalbt.Restaurant.Model.MenuItem;
import com.example.alreadytalbt.Restaurant.Service.RestaurantService;
import com.example.alreadytalbt.Restaurant.dto.CreateRestaurantDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import com.example.alreadytalbt.Restaurant.dto.RestaurantResponseDTO;
import com.example.alreadytalbt.Restaurant.dto.RestaurantUpdateDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/add")
    public ResponseEntity<?> addRestaurant(@RequestBody @Valid CreateRestaurantDTO restaurantDTO) {
        try {
            return ResponseEntity.ok(restaurantService.addRestaurant(restaurantDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }



    @GetMapping("/single/{id}")
    public ResponseEntity<?> getRestaurant(@PathVariable String id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    @GetMapping("/menuItems/{id}")
    public ResponseEntity<List<MenuItemDTO>> getMenu(@PathVariable String id) {
        return ResponseEntity.ok(restaurantService.getMenuItems(id));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDTO>> getAll() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurant(
            @PathVariable String id,
            @RequestBody @Valid RestaurantUpdateDto restaurantResponseDTO) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, restaurantResponseDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable String id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }



}
