package com.example.alreadytalbt.User.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "deliveryGuys")
public class DeliveryGuy extends User {
    private ObjectId Did;

    private ObjectId userId;

    private List<ObjectId> orderIds = new ArrayList<>();

    public ObjectId getDid() {
        return Did;
    }

    public void setDid(ObjectId did) {
        Did = did;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }






    public void addOrderId(ObjectId orderId) {
        if (!this.orderIds.contains(orderId)) {
            this.orderIds.add(orderId);
        }
    }


    public List<ObjectId> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<ObjectId> orderIds) {
        this.orderIds = orderIds;
    }
}
