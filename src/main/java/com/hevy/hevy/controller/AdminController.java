package com.hevy.hevy.controller;

import com.hevy.hevy.dto.request.CreateExerciseRequest;
import com.hevy.hevy.dto.response.ExerciseResponse;
import com.hevy.hevy.dto.response.UserResponse;
import com.hevy.hevy.model.User;
import com.hevy.hevy.service.AdminService;
import com.hevy.hevy.service.ExerciseService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final String SESSION_USER = "currentUser";
    private User currentUser(HttpSession session) {
        return (User) session.getAttribute(SESSION_USER);
    }

    // user management

    @GetMapping("/users")
    public List<UserResponse> getAllUsers(HttpSession session) {
        AdminService adminService = new AdminService();
        adminService.requireAdmin(currentUser(session)); // throw di service, catch di @ExceptionHandler
        return adminService.getAllUsers().stream()
                .map(UserResponse::from)
                .toList();
    }

    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<UserResponse> deactivateUser(
            @PathVariable Long id,
            HttpSession session) {

        AdminService adminService = new AdminService();
        adminService.requireAdmin(currentUser(session));
        return ResponseEntity.ok(UserResponse.from(adminService.deactivateUser(id)));
    }

    @PutMapping("/users/{id}/activate")
    public ResponseEntity<UserResponse> activateUser(
            @PathVariable Long id,
            HttpSession session) {

        AdminService adminService = new AdminService();
        adminService.requireAdmin(currentUser(session));
        return ResponseEntity.ok(UserResponse.from(adminService.activateUser(id)));
    }

    @PutMapping("/users/{id}/reset-password")
    public ResponseEntity<Void> resetPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            HttpSession session) {

        AdminService adminService = new AdminService();
        adminService.requireAdmin(currentUser(session));
        adminService.resetPassword(id, body.get("newPassword"));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> hardDeleteUser(
            @PathVariable Long id,
            HttpSession session) {

        AdminService adminService = new AdminService();
        adminService.requireAdmin(currentUser(session));
        adminService.hardDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ── Global exercise management ───────────────────────────────────────────

    @PostMapping("/exercises")
    public ResponseEntity<ExerciseResponse> createGlobalExercise(
            @RequestBody CreateExerciseRequest req,
            HttpSession session) {

        req.setOwnerId(null); // global = owner null
        AdminService adminService = new AdminService();
        adminService.requireAdmin(currentUser(session));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ExerciseResponse.from(adminService.createGlobalExercise(req)));
    }

    @GetMapping("/exercises")
    public List<ExerciseResponse> getAllExercises(HttpSession session) {
        AdminService adminService = new AdminService();
        adminService.requireAdmin(currentUser(session));
        ExerciseService exerciseService = new ExerciseService();
        return exerciseService.findLibrary(null).stream()
                .map(ExerciseResponse::from)
                .toList();
    }

    @PutMapping("/exercises/{id}")
    public ExerciseResponse updateGlobalExercise(
            @PathVariable Long id,
            @RequestBody CreateExerciseRequest req,
            HttpSession session) {

        AdminService adminService = new AdminService();
        adminService.requireAdmin(currentUser(session));
        return ExerciseResponse.from(adminService.updateGlobalExercise(id, req));
    }

    @DeleteMapping("/exercises/{id}")
    public ResponseEntity<Void> deleteGlobalExercise(
            @PathVariable Long id,
            HttpSession session) {

        AdminService adminService = new AdminService();
        adminService.requireAdmin(currentUser(session));
        adminService.deleteGlobalExercise(id);
        return ResponseEntity.noContent().build();
    }

    //catch exception yang dilempar service

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, String>> handleForbidden(SecurityException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
}