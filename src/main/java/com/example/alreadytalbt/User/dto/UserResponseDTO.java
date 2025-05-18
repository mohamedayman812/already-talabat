// src/main/java/com/example/alreadytalbt/User/dto/UserResponseDTO.java
package com.example.alreadytalbt.User.dto;

public class UserResponseDTO {
    private String id;
    private String username;
    private String email;
    private String address;
    private String phone;
    private String role;

    public UserResponseDTO() {}

    public UserResponseDTO(
            String id,
            String username,
            String email,
            String address,
            String phone,
            String role
    ) {
        this.id       = id;
        this.username = username;
        this.email    = email;
        this.address  = address;
        this.phone    = phone;
        this.role     = role;
    }

    // Getters & Setters

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
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

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
