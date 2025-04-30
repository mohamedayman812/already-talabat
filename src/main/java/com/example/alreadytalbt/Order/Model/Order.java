package com.example.alreadytalbt.Order.Model;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "orders")
public class Order {

    @Id
    private ObjectId id;
    private ObjectId customerId;
    private ObjectId restaurantId;
    @Field("deliveryGuyId")
    private ObjectId deliveryGuyId;
    private List<String> items;
    private String status;
    private String paymentMethod;

    public Order() {}

    public Order(ObjectId customerId, ObjectId restaurantId, ObjectId deliveryGuyId, List<String> items, String status, String paymentMethod) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.deliveryGuyId = deliveryGuyId;
        this.items = items;
        this.status = status;
        this.paymentMethod = paymentMethod;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public ObjectId getCustomerId() { return customerId; }
    public void setCustomerId(ObjectId customerId) { this.customerId = customerId; }

    public ObjectId getRestaurantId() { return restaurantId; }
    public void setRestaurantId(ObjectId restaurantId) { this.restaurantId = restaurantId; }

    public ObjectId getDeliveryGuyId() { return deliveryGuyId; }


    public void setDeliveryGuyId(ObjectId deliveryGuyId) { this.deliveryGuyId = deliveryGuyId; }

    public List<String> getItems() { return items; }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", deliveryGuyId='" + deliveryGuyId + '\'' +
                ", items=" + items +
                ", status='" + status + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }

    public void setItems(List<String> items) { this.items = items; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}