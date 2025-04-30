package com.example.alreadytalbt.Restaurant.dto;


import org.bson.types.ObjectId;

import java.util.List;

public class RestaurantDTO {
    private String id;
    private String name;
    private String address;
    private ObjectId vendorId;
    private List<MenuItemDTO> menuItems;

    // Constructors
    public RestaurantDTO(String id, String name, String address, ObjectId vendorId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.vendorId = vendorId;
    }

    public RestaurantDTO(String id, String name, String address, ObjectId vendorId, List<MenuItemDTO> menuItems) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.vendorId = vendorId;
        this.menuItems = menuItems;
    }
    public RestaurantDTO(){}

    // Getters and setters...

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ObjectId getVendorId() {
        return vendorId;
    }

    public void setVendorId(ObjectId vendorId) {
        this.vendorId = vendorId;
    }

    public List<MenuItemDTO> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemDTO> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", vendorId=" + vendorId +
                ", menuItems=" + menuItems +
                '}';
    }
}
