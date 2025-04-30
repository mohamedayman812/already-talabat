package com.example.alreadytalbt.User.dto;


import org.bson.types.ObjectId;

public class UserResponseDTO {

    private ObjectId id;
    private String name;
    private String email;
    private String address;

    public UserResponseDTO() {}

    public UserResponseDTO(ObjectId id, String name, String email, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
    }

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

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId Id) {
        this.id = Id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

