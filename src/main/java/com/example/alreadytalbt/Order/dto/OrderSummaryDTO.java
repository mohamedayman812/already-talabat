package com.example.alreadytalbt.Order.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

public class OrderSummaryDTO {

    @NotBlank
    @NotNull
    private String id;

    public OrderSummaryDTO(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public OrderSummaryDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    // Constructors, Getters, Setters
}
