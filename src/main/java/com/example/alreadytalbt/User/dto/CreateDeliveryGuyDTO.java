package com.example.alreadytalbt.User.dto;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class CreateDeliveryGuyDTO {

    private List<ObjectId> orderIds = new ArrayList<>();

    public List<ObjectId> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<ObjectId> orderIds) {
        this.orderIds = orderIds;
    }
}
