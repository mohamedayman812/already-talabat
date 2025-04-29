package com.example.alreadytalbt.Restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDTO {
    //shofi 3ayzen ta7otha fen ??
    @Id
    private  String Id;
    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Address must not be blank")
    private String address;

    private List<MenuItemDTO> menuItems= new ArrayList<>();

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

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "Id='" + Id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", menuItems=" + menuItems +
                ", vendorId='" + vendorId + '\'' +
                '}';
    }
}
