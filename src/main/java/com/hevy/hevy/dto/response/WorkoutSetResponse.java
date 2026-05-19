package com.hevy.hevy.dto.response;

import com.hevy.hevy.model.WorkoutSet;

public class WorkoutSetResponse {
    private Long id;
    private Long workoutExerciseId;
    private int setNumber;
    private double weightKg;
    private int reps;

    public static WorkoutSetResponse from(WorkoutSet set) {
        WorkoutSetResponse dto = new WorkoutSetResponse();
        dto.id = set.getId();
        dto.workoutExerciseId = set.getWorkoutExerciseId();
        dto.setNumber = set.getSetNumber();
        dto.weightKg = set.getWeightKg();
        dto.reps = set.getReps();
        return dto;
    }

    public Long getId() { return id; }
    public Long getWorkoutExerciseId() { return workoutExerciseId; }
    public int getSetNumber() { return setNumber; }
    public double getWeightKg() { return weightKg; }
    public int getReps() { return reps; }
}