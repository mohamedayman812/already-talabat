package com.example.alreadytalbt.User.dto;

import com.example.alreadytalbt.Order.Model.Order;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;

import java.util.List;

public class UpdateCustomerDTO {

    private List<ObjectId> orderIds;
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Email(message = "Email should be valid")
    private String email;


    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;


    private String address;

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ObjectId> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<ObjectId> orderIds) {
        this.orderIds = orderIds;
    }
}
