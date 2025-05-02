package com.example.alreadytalbt.Order.dto;

import java.util.List;

public class AddToCartRequestDTO {

    private String customerId;
    private List<String> menuItemIds;

    public AddToCartRequestDTO() {
    }

    public AddToCartRequestDTO(String customerId, List<String> menuItemIds) {
        this.customerId = customerId;
        this.menuItemIds = menuItemIds;
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
