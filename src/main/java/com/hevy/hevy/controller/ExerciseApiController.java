package com.hevy.hevy.controller;

import com.hevy.hevy.dto.request.CreateExerciseRequest;
import com.hevy.hevy.dto.response.ExerciseResponse;
import com.hevy.hevy.model.BaseExercise;
import com.hevy.hevy.service.ExerciseService;
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
@RequestMapping("/api/exercises")
public class ExerciseApiController {

    // GET /api/exercises?userId=1
    @GetMapping
    public List<ExerciseResponse> findAll(@RequestParam(required = false) Long userId) {
        ExerciseService exerciseService = new ExerciseService();
        List<BaseExercise> exercises = exerciseService.findLibrary(userId);
        return exercises.stream()
                .map(ExerciseResponse::from)
                .toList();
    }

    // POST /api/exercises - tambah custom exercise
    @PostMapping
    public ResponseEntity<ExerciseResponse> create(@RequestBody CreateExerciseRequest req) {
        ExerciseService exerciseService = new ExerciseService();
        BaseExercise exercise = exerciseService.create(
                req.getName(),
                req.getCategory(),
                req.getMuscleGroup(),
                req.getEquipment(),
                req.getOwnerId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ExerciseResponse.from(exercise));
    }

    // PUT /api/exercises/{id}
    @PutMapping("/{id}")
    public ExerciseResponse update(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestBody CreateExerciseRequest req) {

        ExerciseService exerciseService = new ExerciseService();
        BaseExercise exercise = exerciseService.update(
                id, userId,
                req.getName(),
                req.getMuscleGroup(),
                req.getEquipment()
        );
        return ExerciseResponse.from(exercise);
    }

    // del /api/exercises/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long userId) {

        ExerciseService exerciseService = new ExerciseService();
        exerciseService.delete(id, userId);
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