package com.example.alreadytalbt.Order.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;

import java.util.List;

public class UpdateOrderDTO {

    @Size(min = 1, max = 50, message = "Customer ID must be between 1 and 50 characters")
    private ObjectId customerId;

    @Size(min = 1, max = 50, message = "Restaurant ID must be between 1 and 50 characters")
    private ObjectId restaurantId;

    @Size(min = 1, max = 50, message = "Delivery Guy ID must be between 1 and 50 characters")
    private ObjectId deliveryGuyId;

    @NotNull(message = "Items list must not be null")
    @Size(min = 1, message = "Items list must contain at least one item")
    private List<String> items;

    @NotBlank(message = "Status must not be blank")
    @Size(max = 20, message = "Status must be at most 20 characters")
    private String status;


    @Size(min = 1, max = 20, message = "Payment method must be between 1 and 20 characters")
    private String paymentMethod;

    public UpdateOrderDTO() {}

    public UpdateOrderDTO(ObjectId customerId, ObjectId restaurantId, ObjectId deliveryGuyId, List<String> items, String status, String paymentMethod) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.deliveryGuyId = deliveryGuyId;
        this.items = items;
        this.status = status;
        this.paymentMethod = paymentMethod;
    }

    public ObjectId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(ObjectId customerId) {
        this.customerId = customerId;
    }

    public ObjectId getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(ObjectId restaurantId) {
        this.restaurantId = restaurantId;
    }

    public ObjectId getDeliveryGuyId() {
        return deliveryGuyId;
    }

    public void setDeliveryGuyId(ObjectId deliveryGuyId) {
        this.deliveryGuyId = deliveryGuyId;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
