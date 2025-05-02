package com.example.alreadytalbt.Order.dto;

import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import java.util.List;

public class UpdateCartDTO {

    private List<String> menuItemIds;
    private String restaurantId;

    // Getters and setters
    public List<String> getMenuItemIds() {
        return menuItemIds;
    }

    public void setMenuItemIds(List<String> menuItemIds) {
        this.menuItemIds = menuItemIds;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
