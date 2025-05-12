package com.example.alreadytalbt.User.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "vendors")
@TypeAlias("vendor")
public class Vendor{

    @Id
    private ObjectId id;

    @Field("userId")
    private ObjectId userId;

    private ObjectId restaurantId;

    public Vendor() {}

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(ObjectId restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "Vendor{" +
                "id=" + id +
                ", userId=" + userId +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
