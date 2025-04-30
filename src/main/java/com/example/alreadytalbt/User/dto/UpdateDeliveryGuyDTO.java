package com.example.alreadytalbt.User.dto;



import com.example.alreadytalbt.User.model.UserRole;

import java.util.ArrayList;
import java.util.List;

public class UpdateDeliveryGuyDTO {

    private String name;


    private String email;


    private String password;


    private String address;

    private List<String> orderIds = new ArrayList<>();



    private UserRole role;
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



    public List<String> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
    }



    public UpdateDeliveryGuyDTO(String name, String email, String password, String address, List<String> orderIds) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.orderIds = orderIds;

    }
}
