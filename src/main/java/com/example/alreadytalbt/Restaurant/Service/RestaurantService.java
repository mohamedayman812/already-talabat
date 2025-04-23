package com.example.alreadytalbt.Restaurant.Service;

import com.example.alreadytalbt.Restaurant.Model.MenuItem;
import com.example.alreadytalbt.Restaurant.Model.Restaurant;
import com.example.alreadytalbt.Restaurant.Repository.MenuItemRepository;
import com.example.alreadytalbt.Restaurant.Repository.RestaurantRepository;
import com.example.alreadytalbt.Restaurant.dto.RestaurantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepo;

    @Autowired
    private MenuItemRepository menuItemRepo;

    public Restaurant addRestaurant(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantDTO.getName());
        restaurant.setAddress(restaurantDTO.getAddress());
        restaurant.setVendorId(restaurantDTO.getVendorId());

        List<MenuItem> savedItems = restaurantDTO.getMenuItems().stream().map(dto -> {
            MenuItem item = new MenuItem();
            item.setName(dto.getName());
            item.setPrice(dto.getPrice());
            item.setDescription(dto.getDescription());
            return menuItemRepo.save(item); // Save each to MongoDB
        }).toList();

        restaurant.setMenuItems(savedItems);
        return restaurantRepo.save(restaurant);
    }


    public List<MenuItem> getMenu(String restaurantId) {
        return restaurantRepo.findById(restaurantId)
                .map(Restaurant::getMenuItems)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepo.findAll();
    }

    // Add, update, delete logic if needed
}
