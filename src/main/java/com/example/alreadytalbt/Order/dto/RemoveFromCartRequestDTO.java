package com.example.alreadytalbt.Order.dto;

public class RemoveFromCartRequestDTO {
    private String customerId;
    private String menuItemId;

    // Constructors (optional)
    public RemoveFromCartRequestDTO() {}

    public RemoveFromCartRequestDTO(String customerId, String menuItemId) {
        this.customerId = customerId;
        this.menuItemId = menuItemId;
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
}
