package com.hevy.hevy.controller;

import com.hevy.hevy.dto.request.CreateExerciseRequest;
import com.hevy.hevy.dto.response.ExerciseResponse;
import com.hevy.hevy.dto.response.UserResponse;
import com.hevy.hevy.model.User;
import com.hevy.hevy.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final String SESSION_USER = "currentUser";

    // Validasi
    private void requireAdmin(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_USER);
        if (user == null) throw new SecurityException("Belum login");
        if (!user.isAdmin()) throw new SecurityException("Akses ditolak — bukan admin");
    }

    //user management

    @GetMapping("/users")
    public List<UserResponse> getAllUsers(HttpSession session) {
        requireAdmin(session);
        AdminService adminService = new AdminService();
        return adminService.getAllUsers().stream()
                .map(UserResponse::from)
                .toList();
    }

    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<UserResponse> deactivateUser(
            @PathVariable Long id,
            HttpSession session) {

        requireAdmin(session);
        AdminService adminService = new AdminService();
        return ResponseEntity.ok(UserResponse.from(adminService.deactivateUser(id)));
    }

    @PutMapping("/users/{id}/activate")
    public ResponseEntity<UserResponse> activateUser(
            @PathVariable Long id,
            HttpSession session) {

        requireAdmin(session);
        AdminService adminService = new AdminService();
        return ResponseEntity.ok(UserResponse.from(adminService.activateUser(id)));
    }

    @PutMapping("/users/{id}/reset-password")
    public ResponseEntity<Void> resetPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            HttpSession session) {

        requireAdmin(session);
        AdminService adminService = new AdminService();
        adminService.resetPassword(id, body.get("newPassword"));
        return ResponseEntity.noContent().build();
    }

    // global exercise

    @PostMapping("/exercises")
    public ResponseEntity<ExerciseResponse> createGlobalExercise(
            @RequestBody CreateExerciseRequest req,
            HttpSession session) {

        requireAdmin(session);
        req.setOwnerId(null); // global = owner null
        AdminService adminService = new AdminService();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ExerciseResponse.from(adminService.createGlobalExercise(req)));
    }

    @PutMapping("/exercises/{id}")
    public ExerciseResponse updateGlobalExercise(
            @PathVariable Long id,
            @RequestBody CreateExerciseRequest req,
            HttpSession session) {

        requireAdmin(session);
        AdminService adminService = new AdminService();
        return ExerciseResponse.from(adminService.updateGlobalExercise(id, req));
    }

    @DeleteMapping("/exercises/{id}")
    public ResponseEntity<Void> deleteGlobalExercise(
            @PathVariable Long id,
            HttpSession session) {

        requireAdmin(session);
        AdminService adminService = new AdminService();
        adminService.deleteGlobalExercise(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, String>> handleForbidden(SecurityException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
}