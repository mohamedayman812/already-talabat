package com.example.alreadytalbt.Order.dto;

import com.example.alreadytalbt.Restaurant.dto.MenuItemDTO;
import java.util.List;

public class CreateCartDTO {
    private String customerId;
    private List<MenuItemDTO> items;

    // Getters and setters
    public String getCustomerId() {
        return customerId;
    }


    public List<MenuItemDTO> getItems() {
        return items;
    }

    public void setItems(List<MenuItemDTO> items) {
        this.items = items;
    }
}
