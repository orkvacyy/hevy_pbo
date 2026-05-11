package com.hevy.hevy.model;

import java.time.LocalDateTime;

public class User {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;
    private boolean active;
    private LocalDateTime createdAt;

    public User(String username, String email, String password) {
        this.username  = username;
        this.email     = email;
        setPassword(password);
        this.role      = "user";   // default role
        this.active    = true;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        if (password == null || password.isBlank()) { // enkap
            throw new IllegalArgumentException("Harap isi password");
        }
        this.password = password;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isAdmin() {
        return "admin".equals(this.role);
    }

    @Override
    public String toString() {
        return "User id= " + id + "\n username='" + username + " role = " + role;
    }
}