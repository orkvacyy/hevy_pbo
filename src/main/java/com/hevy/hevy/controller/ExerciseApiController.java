package com.hevy.hevy.controller;

import com.hevy.hevy.model.BaseExercise;
import com.hevy.hevy.service.ExerciseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseApiController {

    @GetMapping
    public List<Map<String, Object>> findAll(@RequestParam(required = false) Long userId) {
        ExerciseService exerciseService = new ExerciseService();
        List<BaseExercise> exercises = exerciseService.findLibrary(userId);
        return exercises.stream()
                .map(this::toResponse)
                .toList();
    }

    private Map<String, Object> toResponse(BaseExercise exercise) {
        return Map.of(
                "id", exercise.getId(),
                "name", exercise.getName(),
                "category", exercise.getCategory(),
                "muscleGroup", exercise.getMuscleGroup(),
                "equipment", exercise.getEquipment(),
                "scope", exercise.isGlobal() ? "global" : "local",
                "ownerId", exercise.getOwnerId() == null ? "" : exercise.getOwnerId()
        );
    }
}
