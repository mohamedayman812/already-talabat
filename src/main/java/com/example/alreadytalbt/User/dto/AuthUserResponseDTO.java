package com.example.alreadytalbt.User.dto;

import com.example.alreadytalbt.User.Enums.Role;

public class AuthUserResponseDTO {
    private String username;
    private String email;
    private Role role;
    private String token;

    public AuthUserResponseDTO() {}

    public AuthUserResponseDTO(String username, String email, Role role, String token) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.token = token;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
