package com.hevy.hevy.dto.request;

public class AddSetRequest {
    private Long userId;

    // strength
    private double weightKg;
    private int reps;

    // cardio
    private Double durationMinutes;
    private Double distanceKm;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }

    public Double getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Double durationMinutes) { this.durationMinutes = durationMinutes; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

    public boolean isCardio() {
        return durationMinutes != null;
    }
}