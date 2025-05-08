package com.example.alreadytalbt.User.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class CreateCustomerDTO {

    @Id
    private ObjectId userId;

//    @NotBlank(message = "Name is required")
//    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
//    private String name;
//
//    @NotBlank(message = "Email is required")
//    @Email(message = "Email should be valid")
//    private String email;
//
//    @NotBlank(message = "Password is required")
//    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
//    private String password;
//
//    @NotBlank(message = "Address is required")
//    private String address;
//
//    @NotBlank(message = "phone is required")
//    private String phone;

    private List<ObjectId> orderIds = new ArrayList<>();

    // Getters and Setters
    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public List<ObjectId> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<ObjectId> orderIds) {
        this.orderIds = orderIds;
    }


}
