package com.example.alreadytalbt.Order.dto;

import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import java.util.List;

public class CartDTO {
    private String id;
    private String customerId;
    private String restaurantId;
    private List<String> menuItemIds;

    // Getters & Setters

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public List<String> getMenuItemIds() { return menuItemIds; }
    public void setMenuItemIds(List<String> menuItemIds) { this.menuItemIds = menuItemIds; }
}
