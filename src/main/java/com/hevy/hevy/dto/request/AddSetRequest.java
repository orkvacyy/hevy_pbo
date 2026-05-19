package com.hevy.hevy.dto.request;

public class AddSetRequest {
    private Long userId;
    private double weightKg;
    private int reps;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }
}