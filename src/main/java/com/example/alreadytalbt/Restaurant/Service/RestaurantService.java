package com.example.alreadytalbt.Restaurant.Service;

import com.example.alreadytalbt.Restaurant.Model.MenuItem;
import com.example.alreadytalbt.Restaurant.Model.Restaurant;
import com.example.alreadytalbt.Restaurant.Repository.MenuItemRepository;
import com.example.alreadytalbt.Restaurant.Repository.RestaurantRepository;
import com.example.alreadytalbt.Restaurant.dto.CreateRestaurantDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import com.example.alreadytalbt.Restaurant.dto.RestaurantResponseDTO;
import com.example.alreadytalbt.Restaurant.dto.RestaurantUpdateDto;
import com.example.alreadytalbt.User.Enums.Role;
import com.example.alreadytalbt.User.model.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepo;

    @Autowired
    private MenuItemRepository menuItemRepo;

    public RestaurantResponseDTO addRestaurant(CreateRestaurantDTO restaurantDTO ) {
        if (restaurantDTO == null || !StringUtils.hasText(restaurantDTO.getVendorId())) {
            throw new IllegalArgumentException("Invalid restaurant data");
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantDTO.getRestaurantName());
        restaurant.setAddress(restaurantDTO.getRestaurantAddress());
        restaurant.setVendorId(new ObjectId(restaurantDTO.getVendorId()));
        restaurant.setMenuItems(new ArrayList<>());

        List<MenuItemDTO> menuDTOs = new ArrayList<>();

        if (restaurantDTO.getMenuItems() != null) {
            for (MenuItemDTO dto : restaurantDTO.getMenuItems()) {
                MenuItem item = new MenuItem();
                item.setName(dto.getName());
                item.setPrice(dto.getPrice());
                item.setDescription(dto.getDescription());
                item.setRestaurantId(new ObjectId(dto.getRestauarantId()));
                MenuItem savedItem = menuItemRepo.save(item);

                restaurant.getMenuItems().add(savedItem.getId());
                menuDTOs.add(mapToMenuItemDTO(savedItem));
            }
        }

        Restaurant saved = restaurantRepo.save(restaurant);
        return mapToRestaurantDTO(saved, menuDTOs);
    }

    public List<MenuItemDTO> getMenuItems(String restaurantId) {
        if (!StringUtils.hasText(restaurantId)) {
            throw new IllegalArgumentException("Restaurant ID cannot be empty");
        }

        Restaurant restaurant = restaurantRepo.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));

        // Convert the list of ObjectIds to MenuItemDTOs
        List<MenuItemDTO> menuItemDTOs = menuItemRepo.findAllById(restaurant.getMenuItems().stream().collect(Collectors.toList())
                ).stream()
                .map(this::mapToMenuItemDTO)  // Assuming mapToMenuItemDTO takes a single MenuItem
                .collect(Collectors.toList());

        return menuItemDTOs;
    }

    public List<RestaurantResponseDTO> getAllRestaurants() {
        return restaurantRepo.findAll().stream()
                .map(restaurant -> {
                    List<MenuItem> items = restaurant.getMenuItems() == null ?
                            new ArrayList<>() :
                            menuItemRepo.findAllById(
                                    new ArrayList<>(restaurant.getMenuItems())
                            );

                    List<MenuItemDTO> menuDTOs = items.stream()
                            .map(this::mapToMenuItemDTO)
                            .collect(Collectors.toList());

                    return mapToRestaurantDTO(restaurant, menuDTOs);
                })
                .collect(Collectors.toList());
    }

    public Optional<RestaurantResponseDTO> getRestaurantById(String id) {
        if (!StringUtils.hasText(id)) {
            return Optional.empty();
        }

        return restaurantRepo.findById(id)
                .map(restaurant -> {
                    List<MenuItemDTO> menuDTOs = new ArrayList<>();
                    if (restaurant.getMenuItems() != null && !restaurant.getMenuItems().isEmpty()) {
                        List<MenuItem> items = menuItemRepo.findAllById(
                                new ArrayList<>(restaurant.getMenuItems())
                        );
                        menuDTOs = items.stream()
                                .map(this::mapToMenuItemDTO)
                                .collect(Collectors.toList());
                    }

                    return mapToRestaurantDTO(restaurant, menuDTOs);
                });
    }

    public RestaurantResponseDTO updateRestaurant(String id, RestaurantUpdateDto restaurantResponseDTO) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("Restaurant ID cannot be empty");
        }

        Restaurant restaurant = restaurantRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));

        if (StringUtils.hasText(restaurantResponseDTO.getName())) {
            restaurant.setName(restaurantResponseDTO.getName());
        }
        if (restaurantResponseDTO.getAddress() != null) {  // Assuming price is Float/Double wrapper class
            restaurant.setAddress(restaurantResponseDTO.getAddress());
        }


        Restaurant saved = restaurantRepo.save(restaurant);

        List<MenuItemDTO> menuDTOs = new ArrayList<>();
        if (restaurant.getMenuItems() != null && !restaurant.getMenuItems().isEmpty()) {
            menuDTOs = menuItemRepo.findAllById(
                            new ArrayList<>(restaurant.getMenuItems())
                    ).stream()
                    .map(item -> new MenuItemDTO(item.getId().toHexString(),item.getName(), item.getPrice(), item.getDescription(),item.getRestaurantId().toHexString()))
                    .collect(Collectors.toList());
        }

        return new RestaurantResponseDTO(
                saved.getId().toHexString(),
                saved.getName(),
                saved.getAddress(),
                saved.getVendorId().toHexString(),
                menuDTOs
        );
    }

    public void deleteRestaurant(String id) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("Restaurant ID cannot be empty");
        }

        Restaurant restaurant = restaurantRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));

        if (restaurant.getMenuItems() != null && !restaurant.getMenuItems().isEmpty()) {
            menuItemRepo.deleteAllById(
                    new ArrayList<>(restaurant.getMenuItems())
            );
        }

        restaurantRepo.deleteById(id);
    }



    // Helper methods
    private MenuItemDTO mapToMenuItemDTO(MenuItem item) {
        return new MenuItemDTO(item.getId().toHexString(),item.getName(), item.getPrice(), item.getDescription(),item.getRestaurantId().toHexString());
    }

    private RestaurantResponseDTO mapToRestaurantDTO(Restaurant restaurant, List<MenuItemDTO> menuItems) {
        return new RestaurantResponseDTO(
                restaurant.getId().toString(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getVendorId().toHexString(),
                menuItems
        );
    }
}