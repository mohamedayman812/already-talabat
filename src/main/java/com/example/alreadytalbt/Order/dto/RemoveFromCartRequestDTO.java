package com.example.alreadytalbt.Order.dto;

public class RemoveFromCartRequestDTO {
    private String customerId;
    private String menuItemId;
    private String restaurantId;
    // Constructors (optional)
    public RemoveFromCartRequestDTO() {}

    public RemoveFromCartRequestDTO(String customerId,String restaurantId ,String menuItemId ) {
        this.customerId = customerId;
        this.menuItemId = menuItemId;
        this.restaurantId = restaurantId;
    }

    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
