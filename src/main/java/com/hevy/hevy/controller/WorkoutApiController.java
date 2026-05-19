package com.hevy.hevy.controller;

import com.hevy.hevy.model.WorkoutExercise;
import com.hevy.hevy.model.WorkoutSession;
import com.hevy.hevy.model.WorkoutSet;
import com.hevy.hevy.service.WorkoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutApiController {

    @GetMapping
    public List<Map<String, Object>> findByUser(@RequestParam Long userId) {
        WorkoutService workoutService = new WorkoutService();
        return workoutService.findSessionsByUser(userId).stream()
                .map(this::toSessionResponse)
                .toList();
    }

    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> findActive(@RequestParam Long userId) {
        WorkoutService workoutService = new WorkoutService();
        return workoutService.findActiveSession(userId)
                .map(session -> ResponseEntity.ok(toSessionResponse(session)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/start")
    public Map<String, Object> start(@RequestBody StartSessionRequest request) {
        WorkoutService workoutService = new WorkoutService();
        return toSessionResponse(workoutService.startSession(request.userId()));
    }

    @PostMapping("/{sessionId}/exercises")
    public Map<String, Object> addExercise(@PathVariable Long sessionId, @RequestBody AddExerciseRequest request) {
        WorkoutService workoutService = new WorkoutService();
        return toWorkoutExerciseResponse(workoutService.addExercise(request.userId(), sessionId, request.exerciseId()));
    }

    @PostMapping("/{sessionId}/finish")
    public Map<String, Object> finish(@PathVariable Long sessionId, @RequestBody FinishSessionRequest request) {
        WorkoutService workoutService = new WorkoutService();
        return toSessionResponse(workoutService.finishSession(request.userId(), sessionId, request.notes()));
    }

    @PostMapping("/exercises/{workoutExerciseId}/sets")
    public ResponseEntity<Map<String, Object>> addSet(
            @PathVariable Long workoutExerciseId,
            @RequestBody AddSetRequest request) {

        WorkoutService workoutService = new WorkoutService();
        WorkoutSet set = workoutService.addSet(request.userId(), workoutExerciseId, request.weightKg(), request.reps());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id",                 set.getId(),
                "workoutExerciseId",  set.getWorkoutExerciseId(),
                "setNumber",          set.getSetNumber(),
                "weightKg",           set.getWeightKg(),
                "reps",               set.getReps()
        ));
    }

    @DeleteMapping("/exercises/sets/{setId}")
    public ResponseEntity<Void> deleteSet(
            @PathVariable Long setId,
            @RequestParam Long userId) {

        WorkoutService workoutService = new WorkoutService();
        workoutService.deleteSet(userId, setId);
        return ResponseEntity.noContent().build();
    }

    public record AddSetRequest(Long userId, double weightKg, int reps) {}

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> cancel(@PathVariable Long sessionId, @RequestParam Long userId) {
        WorkoutService workoutService = new WorkoutService();
        workoutService.cancelSession(userId, sessionId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(RuntimeException error) {
        return ResponseEntity.badRequest().body(Map.of("message", error.getMessage()));
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, String>> handleForbidden(SecurityException error) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", error.getMessage()));
    }

    private Map<String, Object> toSessionResponse(WorkoutSession session) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", session.getId());
        response.put("userId", session.getUserId());
        response.put("notes", session.getNotes());
        response.put("startedAt", session.getStartedAt());
        response.put("finishedAt", session.getFinishedAt());
        response.put("active", session.isActive());
        response.put("paused", session.isPaused());
        response.put("durationMinutes", session.getDurationMinutes());
        response.put("exercises", session.getExercises().stream().map(this::toWorkoutExerciseResponse).toList());
        return response;
    }

    private Map<String, Object> toWorkoutExerciseResponse(WorkoutExercise workoutExercise) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", workoutExercise.getId());
        response.put("sessionId", workoutExercise.getSessionId());
        response.put("exerciseId", workoutExercise.getExerciseId());
        response.put("exerciseNameSnapshot", workoutExercise.getExerciseNameSnapshot());
        response.put("orderIndex", workoutExercise.getOrderIndex());
        response.put("sets", workoutExercise.getSets().stream().map(this::toSetResponse).toList());
        return response;
    }

    private Map<String, Object> toSetResponse(WorkoutSet set) {
        return Map.of(
                "id", set.getId(),
                "workoutExerciseId", set.getWorkoutExerciseId(),
                "setNumber", set.getSetNumber(),
                "weightKg", set.getWeightKg(),
                "reps", set.getReps()
        );
    }

    public record StartSessionRequest(Long userId) {
    }

    public record AddExerciseRequest(Long userId, Long exerciseId) {
    }

    public record FinishSessionRequest(Long userId, String notes) {
    }
}
