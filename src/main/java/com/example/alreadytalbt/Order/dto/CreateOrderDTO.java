package com.example.alreadytalbt.Order.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CreateOrderDTO {

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotBlank(message = "Restaurant ID is required")
    private String restaurantId;

    @NotBlank(message = "Delivery Guy ID is required")
    private String deliveryGuyId;

    @NotNull(message = "Items list is required")
    private List<String> items;

    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    // Getters and setters
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
