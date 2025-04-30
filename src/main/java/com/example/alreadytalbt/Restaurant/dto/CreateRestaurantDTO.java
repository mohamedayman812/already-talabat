package com.example.alreadytalbt.Restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class CreateRestaurantDTO {
    @NotBlank(message = "Name must not be blank")
    private String restaurantName;

    @NotBlank(message = "Address must not be blank")
    private String restaurantAddress;

    private List<MenuItemDTO> menuItems= new ArrayList<>();

    private ObjectId vendorId;

    // Getters and Setters
    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }



    public List<MenuItemDTO> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemDTO> menuItems) {
        this.menuItems = menuItems;
    }

    public ObjectId getVendorId() {
        return vendorId;
    }

    public void setVendorId(ObjectId vendorId) {
        this.vendorId = vendorId;
    }

    @Override
    public String toString() {
        return "CreateRestaurantDTO{" +
                "restaurantName='" + restaurantName + '\'' +
                ", restaurantAddress='" + restaurantAddress + '\'' +
                ", menuItems=" + menuItems +
                ", vendorId=" + vendorId +
                '}';
    }
}
