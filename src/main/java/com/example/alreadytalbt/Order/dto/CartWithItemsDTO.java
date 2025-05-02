package com.example.alreadytalbt.Order.dto;

import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import java.util.List;

public class CartWithItemsDTO {
    private String id;
    private String customerId;
    private String restaurantId;
    private List<MenuItemDTO> items;

    // Getters and setters

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<MenuItemDTO> getItems() {
        return items;
    }

    public void setItems(List<MenuItemDTO> items) {
        this.items = items;
    }
}
