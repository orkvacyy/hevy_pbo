package com.hevy.hevy.dto.response;

import com.hevy.hevy.model.User;

public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String role;

    //convert dari model ke dto
    public static UserResponse from(User user) {
        UserResponse dto = new UserResponse();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.email = user.getEmail();
        dto.role = user.getRole();
        return dto;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}