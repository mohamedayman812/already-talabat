package com.example.alreadytalbt.User.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vendors")
public class Vendor extends User {
    private String restaurantId;


    // Getters and Setters

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

}
