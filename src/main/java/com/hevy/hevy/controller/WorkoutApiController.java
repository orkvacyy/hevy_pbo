package com.hevy.hevy.controller;

import com.hevy.hevy.dto.request.AddExerciseRequest;
import com.hevy.hevy.dto.request.AddSetRequest;
import com.hevy.hevy.dto.request.FinishSessionRequest;
import com.hevy.hevy.dto.request.StartSessionRequest;
import com.hevy.hevy.dto.response.WorkoutExerciseResponse;
import com.hevy.hevy.dto.response.WorkoutSessionResponse;
import com.hevy.hevy.dto.response.WorkoutSetResponse;
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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutApiController {

    // GET /api/workouts?userId=1 — riwayat sesi user
    @GetMapping
    public List<WorkoutSessionResponse> findByUser(@RequestParam Long userId) {
        WorkoutService workoutService = new WorkoutService();
        return workoutService.findSessionsByUser(userId).stream()
                .map(WorkoutSessionResponse::from)
                .toList();
    }

    // GET /api/workouts/active?userId=1
    @GetMapping("/active")
    public ResponseEntity<WorkoutSessionResponse> findActive(@RequestParam Long userId) {
        WorkoutService workoutService = new WorkoutService();
        return workoutService.findActiveSession(userId)
                .map(session -> ResponseEntity.ok(WorkoutSessionResponse.from(session)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    // POST /api/workouts/start
    @PostMapping("/start")
    public WorkoutSessionResponse start(@RequestBody StartSessionRequest req) {
        WorkoutService workoutService = new WorkoutService();
        return WorkoutSessionResponse.from(workoutService.startSession(req.getUserId()));
    }

    // POST /api/workouts/{sessionId}/exercises
    @PostMapping("/{sessionId}/exercises")
    public WorkoutExerciseResponse addExercise(
            @PathVariable Long sessionId,
            @RequestBody AddExerciseRequest req) {

        WorkoutService workoutService = new WorkoutService();
        return WorkoutExerciseResponse.from(
                workoutService.addExercise(req.getUserId(), sessionId, req.getExerciseId())
        );
    }

    // POST /api/workouts/exercises/{workoutExerciseId}/sets
    @PostMapping("/exercises/{workoutExerciseId}/sets")
    public ResponseEntity<WorkoutSetResponse> addSet(
            @PathVariable Long workoutExerciseId,
            @RequestBody AddSetRequest req) {

        WorkoutService workoutService = new WorkoutService();
        return ResponseEntity.status(HttpStatus.CREATED).body(
                WorkoutSetResponse.from(
                        workoutService.addSet(req.getUserId(), workoutExerciseId, req.getWeightKg(), req.getReps())
                )
        );
    }

    // DELETE /api/workouts/exercises/sets/{setId}
    @DeleteMapping("/exercises/sets/{setId}")
    public ResponseEntity<Void> deleteSet(
            @PathVariable Long setId,
            @RequestParam Long userId) {

        WorkoutService workoutService = new WorkoutService();
        workoutService.deleteSet(userId, setId);
        return ResponseEntity.noContent().build();
    }

    // POST /api/workouts/{sessionId}/finish
    @PostMapping("/{sessionId}/finish")
    public WorkoutSessionResponse finish(
            @PathVariable Long sessionId,
            @RequestBody FinishSessionRequest req) {

        WorkoutService workoutService = new WorkoutService();
        return WorkoutSessionResponse.from(
                workoutService.finishSession(req.getUserId(), sessionId, req.getNotes())
        );
    }

    // DELETE /api/workouts/{sessionId} — cancel sesi
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> cancel(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {

        WorkoutService workoutService = new WorkoutService();
        workoutService.cancelSession(userId, sessionId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, String>> handleBadRequest(RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, String>> handleForbidden(SecurityException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
    }
}