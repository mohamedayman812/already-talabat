package com.example.alreadytalbt.Restaurant.Service;


import com.example.alreadytalbt.Restaurant.Model.MenuItem;
import com.example.alreadytalbt.Restaurant.Model.Restaurant;
import com.example.alreadytalbt.Restaurant.Repository.MenuItemRepository;
import com.example.alreadytalbt.Restaurant.Repository.RestaurantRepository;
import com.example.alreadytalbt.Restaurant.dto.MenuItemCreateDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemUpdateDTO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepo;

    @Autowired
    private RestaurantRepository restaurantRepo;

    public MenuItemDTO addMenuItem(MenuItemCreateDTO dto) {
        System.out.println(dto.getRestaurantId()==null ? "null": dto.getRestaurantId());
        Restaurant restaurant = restaurantRepo.findById(dto.getRestaurantId()).orElseThrow(() -> new RuntimeException("Restaurant not found"));

        MenuItem item = new MenuItem();
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setDescription(dto.getDescription());
        item.setRestaurantId(new ObjectId(dto.getRestaurantId()));

        MenuItem savedItem = menuItemRepo.save(item);
        restaurant.getMenuItems().add(savedItem.getId());
        restaurantRepo.save(restaurant);
        System.out.println(savedItem.toString());
        return toDTO(savedItem);
    }

    public MenuItemDTO updateMenuItem(String menuItemId, MenuItemUpdateDTO dto) {
        MenuItem item = menuItemRepo.findById(new ObjectId(menuItemId)).orElseThrow(() -> new RuntimeException("Menu item not found"));


        if (StringUtils.hasText(dto.getName())) {
            item.setName(dto.getName());
        }
        if (dto.getPrice() != null) {
            item.setPrice(dto.getPrice());
        }
        if (StringUtils.hasText(dto.getDescription())) {
            item.setDescription(dto.getDescription());
        }

        MenuItem updated = menuItemRepo.save(item);
        return toDTO(updated);
    }

    public void deleteMenuItem(String menuItemId) {
        if (!StringUtils.hasText(menuItemId)) {
            throw new IllegalArgumentException("Menu item ID cannot be empty");
        }
        ObjectId objectId = new ObjectId(menuItemId);

        List<Restaurant> restaurants = restaurantRepo.findAll();
        for (Restaurant restaurant : restaurants) {
            System.out.println(restaurant.toString());
            System.out.println(Arrays.toString(restaurant.getMenuItems().toArray()));
        }
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getMenuItems().remove(objectId)) {
                restaurantRepo.save(restaurant);
            }
        }

        menuItemRepo.deleteById(objectId);
    }

    private MenuItemDTO toDTO(MenuItem item) {
        return new MenuItemDTO(
                item.getId().toHexString(),
                item.getName(),
                item.getPrice(),
                item.getDescription(),
                item.getRestaurantId().toHexString()
        );
    }

    public Optional<MenuItemDTO> getMenuItemById(String menuItemId) {
        return menuItemRepo.findById(new ObjectId(menuItemId)).map(this::toDTO);
    }
}

