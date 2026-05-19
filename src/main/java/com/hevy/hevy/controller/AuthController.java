package com.hevy.hevy.controller;

import com.hevy.hevy.dto.request.LoginRequest;
import com.hevy.hevy.dto.request.RegisterRequest;
import com.hevy.hevy.dto.response.UserResponse;
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

    private static final String SESSION_USER = "currentUser";

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody RegisterRequest req,
            HttpSession session) {

        AuthService authService = new AuthService();
        User user = authService.register(req.getUsername(), req.getEmail(), req.getPassword());
        session.setAttribute(SESSION_USER, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @RequestBody LoginRequest req,
            HttpSession session) {

        AuthService authService = new AuthService();
        User user = authService.login(req.getEmail(), req.getPassword());
        session.setAttribute(SESSION_USER, user);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
}