package com.example.alreadytalbt.Order.dto;

import org.bson.types.ObjectId;

public class UpdateOrderStatusDTO {
    private ObjectId orderId;

    public ObjectId getOrderId() {
        return orderId;
    }

    public void setOrderId(ObjectId orderId) {
        this.orderId = orderId;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    private String newStatus;

    // Getters and setters
}
