package com.example.alreadytalbt.User.dto;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class UpdateDeliveryGuyDTO {


    private String Deliveryid;
    private String UserId;
    private String name;


    private String email;
    private String phone;
    private String address;
    private List<ObjectId> orderIds = new ArrayList<>();


    public String getDeliveryid() {
        return Deliveryid;
    }

    public void setDeliveryid(String deliveryid) {
        Deliveryid = deliveryid;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

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

    public UpdateDeliveryGuyDTO() {
    }

    public UpdateDeliveryGuyDTO(String name, String email, String password, String address, List<ObjectId> orderIds) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.orderIds = orderIds;

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
