package com.example.alreadytalbt.Order.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class AddToCartRequestDTO {

    private String customerId;

    @NotBlank(message = "Restaurant ID is required")
    private String restaurantId;
    @NotEmpty(message = "Menu item list cannot be empty")
    private List<String> menuItemIds;

    public AddToCartRequestDTO() {
    }

    public AddToCartRequestDTO(String customerId, String restaurantId,List<String> menuItemIds) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.menuItemIds = menuItemIds;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<String> getMenuItemIds() {
        return menuItemIds;
    }

    public void setMenuItemIds(List<String> menuItemIds) {
        this.menuItemIds = menuItemIds;
    }
}
