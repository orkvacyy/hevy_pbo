package com.hevy.hevy.dto.response;

import com.hevy.hevy.model.BaseExercise;

public class ExerciseResponse {
    private Long id;
    private String name;
    private String category;
    private String muscleGroup;
    private String equipment;
    private String scope;
    private Long ownerId;

    public static ExerciseResponse from(BaseExercise exercise) {
        ExerciseResponse dto = new ExerciseResponse();
        dto.id = exercise.getId();
        dto.name = exercise.getName();
        dto.category = exercise.getCategory();
        dto.muscleGroup = exercise.getMuscleGroup();
        dto.equipment = exercise.getEquipment();
        dto.scope = exercise.isGlobal() ? "global" : "local";
        dto.ownerId = exercise.getOwnerId();
        return dto;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getMuscleGroup() { return muscleGroup; }
    public String getEquipment() { return equipment; }
    public String getScope() { return scope; }
    public Long getOwnerId() { return ownerId; }
}