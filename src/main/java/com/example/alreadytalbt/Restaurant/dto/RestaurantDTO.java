package com.example.alreadytalbt.Restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class RestaurantDTO {
    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Address must not be blank")
    private String address;

    private List<MenuItemDTO> menuItems;

    private String vendorId;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<MenuItemDTO> getMenuItems() { return menuItems; }
    public void setMenuItems(List<MenuItemDTO> menuItems) { this.menuItems = menuItems; }

    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }
}
