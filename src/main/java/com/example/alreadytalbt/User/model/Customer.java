package com.example.alreadytalbt.User.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Customers")
public class Customer extends User {

    private List<ObjectId> orderIds = new ArrayList<>();
    private ObjectId userId; // Reference to shared user data

    public List<ObjectId> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<ObjectId> orderIds) {
        this.orderIds = orderIds;
    }

    public void addOrderId(ObjectId orderId) {
        if (!this.orderIds.contains(orderId)) {
            this.orderIds.add(orderId);
        }
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }
}
