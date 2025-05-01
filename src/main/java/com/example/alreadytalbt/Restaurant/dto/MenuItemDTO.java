package com.example.alreadytalbt.Restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MenuItemDTO {

    private String id;
    private String name;
    private float price;
    private String description;
    private String restauarantId;


    public MenuItemDTO() {}

    public MenuItemDTO(String id, String name, float price, String description, String restauarantId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.restauarantId=restauarantId;
    }
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getRestauarantId() {
        return restauarantId;
    }

    public void setRestauarantId(String restauarantId) {
        this.restauarantId = restauarantId;
    }
}
