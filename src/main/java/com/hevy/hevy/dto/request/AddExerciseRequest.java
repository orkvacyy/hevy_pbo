package com.hevy.hevy.dto.request;

public class AddExerciseRequest {
    private Long userId;
    private Long exerciseId;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getExerciseId() { return exerciseId; }
    public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }
}