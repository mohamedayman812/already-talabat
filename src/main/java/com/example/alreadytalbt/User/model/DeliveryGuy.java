package com.example.alreadytalbt.User.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "deliveryGuys")
public class DeliveryGuy extends User {



    private List<String> orderIds = new ArrayList<>();


    public DeliveryGuy(String id, String name,String address ,String email, String password, UserRole role, List<String> orderIds) {
        super(id, name, email, password, address, role);
        this.orderIds = orderIds;
    }

    public void addOrderId(String orderId) {
        if (!this.orderIds.contains(orderId)) {
            this.orderIds.add(orderId);
        }
    }


    public List<String> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
    }
}
