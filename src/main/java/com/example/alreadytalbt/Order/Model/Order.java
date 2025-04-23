package com.example.alreadytalbt.Order.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private String customerId;
    private String restaurantId;
    private String deliveryGuyId;
    private List<String> items;
    private String status;
    private String paymentMethod;

    public Order() {}

    public Order(String customerId, String restaurantId, String deliveryGuyId, List<String> items, String status, String paymentMethod) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.deliveryGuyId = deliveryGuyId;
        this.items = items;
        this.status = status;
        this.paymentMethod = paymentMethod;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public String getDeliveryGuyId() { return deliveryGuyId; }
    public void setDeliveryGuyId(String deliveryGuyId) { this.deliveryGuyId = deliveryGuyId; }

    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}