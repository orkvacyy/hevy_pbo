package com.hevy.hevy.controller;

import com.hevy.hevy.model.User;
import com.hevy.hevy.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // cookies
    private static final String SESSION_USER = "currentUser";

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestBody RegisterRequest req,
            HttpSession session) {

        AuthService authService = new AuthService();
        User user = authService.register(req.username(), req.email(), req.password());
        session.setAttribute(SESSION_USER, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody LoginRequest req,
            HttpSession session) {

        AuthService authService = new AuthService();
        User user = authService.login(req.email(), req.password());
        session.setAttribute(SESSION_USER, user);
        return ResponseEntity.ok(toResponse(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    // cek siapa yg login
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(toResponse(user));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }

    private Map<String, Object> toResponse(User user) {
        return Map.of(
                "id",       user.getId(),
                "username", user.getUsername(),
                "email",    user.getEmail(),
                "role",     user.getRole()
        );
    }

    public record RegisterRequest(String username, String email, String password) {}
    public record LoginRequest(String email, String password) {}
}