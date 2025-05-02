package com.example.alreadytalbt.Order.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;


import java.util.List;

public class CreateOrderDTO {

    @NotBlank(message = "Customer ID is required")
    private ObjectId customerId;

    @NotBlank(message = "Restaurant ID is required")
    private ObjectId restaurantId;

    @NotBlank(message = "Cart ID is required")
    private ObjectId cartId;

    @NotBlank(message = "Delivery Guy ID is required")
    private ObjectId deliveryGuyId;

    @NotNull(message = "Items list is required")
    private List<ObjectId> items;

    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    // Getters and setters

    public ObjectId getCartId() {
        return cartId;
    }

    public void setCartId(ObjectId cartId) {
        this.cartId = cartId;
    }

    public ObjectId getCustomerId() { return customerId; }
    public void setCustomerId(ObjectId customerId) { this.customerId = customerId; }

    public ObjectId getRestaurantId() { return restaurantId; }
    public void setRestaurantId(ObjectId restaurantId) { this.restaurantId = restaurantId; }

    public ObjectId getDeliveryGuyId() { return deliveryGuyId; }
    public void setDeliveryGuyId(ObjectId deliveryGuyId) { this.deliveryGuyId = deliveryGuyId; }

    public List<ObjectId> getItems() { return items; }
    public void setItems(List<ObjectId> items) { this.items = items; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
