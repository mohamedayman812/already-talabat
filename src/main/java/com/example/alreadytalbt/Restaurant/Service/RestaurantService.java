package com.example.alreadytalbt.Restaurant.Service;

import com.example.alreadytalbt.Restaurant.Model.MenuItem;
import com.example.alreadytalbt.Restaurant.Model.Restaurant;
import com.example.alreadytalbt.Restaurant.Repository.MenuItemRepository;
import com.example.alreadytalbt.Restaurant.Repository.RestaurantRepository;
import com.example.alreadytalbt.Restaurant.dto.CreateRestaurantDTO;
import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import com.example.alreadytalbt.Restaurant.dto.RestaurantDTO;
import com.example.alreadytalbt.User.dto.UserResponseDTO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepo;

    @Autowired
    private MenuItemRepository menuItemRepo;

    public RestaurantDTO addRestaurant(CreateRestaurantDTO restaurantDTO) {
        Restaurant restaurant = new Restaurant();
        System.out.println("stri nngg:"+restaurantDTO.toString());
        restaurant.setName(restaurantDTO.getRestaurantName());
        restaurant.setAddress(restaurantDTO.getRestaurantAddress());
        System.out.println("vendor in rest :"+ restaurantDTO.getVendorId());
        restaurant.setVendorId(new ObjectId(restaurantDTO.getVendorId()));

        List<MenuItem> savedItems = new ArrayList<>();
        if (restaurantDTO.getMenuItems() != null) {
            savedItems = restaurantDTO.getMenuItems().stream().map(dto -> {
                MenuItem item = new MenuItem();
                item.setName(dto.getName());
                item.setPrice(dto.getPrice());
                item.setDescription(dto.getDescription());
                return menuItemRepo.save(item);
            }).toList();
        }

        restaurant.setMenuItems(savedItems);
        Restaurant saved = restaurantRepo.save(restaurant);


        List<MenuItemDTO> menuDTOs = savedItems.stream().map(item ->
                new MenuItemDTO(item.getName(), item.getPrice(), item.getDescription())
        ).toList();

        return new RestaurantDTO(
                saved.getId().toHexString(),
                saved.getName(),
                saved.getAddress(),
                saved.getVendorId(),
                menuDTOs
        );
    }

    public List<MenuItem> getMenu(String restaurantId) {
        return restaurantRepo.findById(restaurantId)
                .map(Restaurant::getMenuItems)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    public List<RestaurantDTO> getAllRestaurants() {
            return restaurantRepo.findAll().stream().map(restaurant -> {
                List<MenuItemDTO> menuDTOs = restaurant.getMenuItems().stream().map(item ->
                        new MenuItemDTO(item.getName(), item.getPrice(), item.getDescription())
                ).toList();

                return new RestaurantDTO(
                        restaurant.getId().toHexString(),
                        restaurant.getName(),
                        restaurant.getAddress(),
                        restaurant.getVendorId(),
                        menuDTOs
                );
            }).toList();


    }

    public RestaurantDTO updateRestaurant(String id, RestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        restaurant.setName(restaurantDTO.getName());
        restaurant.setAddress(restaurantDTO.getAddress());
        restaurant.setVendorId(restaurantDTO.getVendorId());

        List<MenuItem> updatedItems = restaurantDTO.getMenuItems().stream().map(dto -> {
            MenuItem item = new MenuItem();
            item.setName(dto.getName());
            item.setPrice(dto.getPrice());
            item.setDescription(dto.getDescription());
            return menuItemRepo.save(item);
        }).toList();

        restaurant.setMenuItems(updatedItems);
        Restaurant saved = restaurantRepo.save(restaurant);

        List<MenuItemDTO> menuDTOs = updatedItems.stream().map(item ->
                new MenuItemDTO(item.getName(), item.getPrice(), item.getDescription())
        ).toList();

        return new RestaurantDTO(
                saved.getId().toHexString(),
                saved.getName(),
                saved.getAddress(),
                saved.getVendorId(),
                menuDTOs
        );
    }

    public void deleteRestaurant(String id) {
        Restaurant restaurant = restaurantRepo.findById(id).orElseThrow(() -> new RuntimeException("Restaurant not found"));

        restaurant.getMenuItems().forEach(item -> menuItemRepo.deleteById(item.getId()));

        restaurantRepo.deleteById(id);
    }
    public Optional<RestaurantDTO> getRestaurantById(String id) {

        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }


        return restaurantRepo.findById(id)
                .map(restaurant -> {

                    if (restaurant.getId() == null) {
                        throw new IllegalStateException("Restaurant exists but has null ID");
                    }

                    return new RestaurantDTO(
                            restaurant.getId().toHexString(),
                            restaurant.getName(),
                            restaurant.getAddress(),
                            restaurant.getVendorId()
                    );
                });
    }



}
