package com.hevy.hevy.model;

public class WorkoutSet {

    private Long id;
    private Long workoutExerciseId;
    private int setNumber;

    // strength fields
    private double weightKg;
    private int reps;

    // cardio fields (null kalau strength)
    private Double durationMinutes;
    private Double distanceKm;

    // Constructor untuk strength
    public WorkoutSet(Long workoutExerciseId, int setNumber, double weightKg, int reps) {
        this.workoutExerciseId = workoutExerciseId;
        this.setNumber         = setNumber;
        setWeightKg(weightKg);
        setReps(reps);
        this.durationMinutes   = null;
        this.distanceKm        = null;
    }

    // Constructor untuk cardio
    public WorkoutSet(Long workoutExerciseId, int setNumber, double durationMinutes, double distanceKm, boolean isCardio) {
        this.workoutExerciseId = workoutExerciseId;
        this.setNumber         = setNumber;
        this.weightKg          = 0;
        this.reps              = 0;
        setDurationMinutes(durationMinutes);
        setDistanceKm(distanceKm);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getWorkoutExerciseId() { return workoutExerciseId; }
    public void setWorkoutExerciseId(Long workoutExerciseId) { this.workoutExerciseId = workoutExerciseId; }

    public int getSetNumber() { return setNumber; }
    public void setSetNumber(int setNumber) { this.setNumber = setNumber; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) {
        if (weightKg < 0) throw new IllegalArgumentException("Berat tidak boleh negatif");
        this.weightKg = weightKg;
    }

    public int getReps() { return reps; }
    public void setReps(int reps) {
        if (reps < 0) throw new IllegalArgumentException("Reps tidak bolhe negatif");
        this.reps = reps;
    }

    public Double getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Double durationMinutes) {
        if (durationMinutes != null && durationMinutes < 0)
            throw new IllegalArgumentException("Durasi tidak boleh negatif");
        this.durationMinutes = durationMinutes;
    }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) {
        if (distanceKm != null && distanceKm < 0)
            throw new IllegalArgumentException("Jarak tidak boleh negatif");
        this.distanceKm = distanceKm;
    }

    public boolean isCardio() {
        return durationMinutes != null;
    }

    @Override
    public String toString() {
        if (isCardio()) {
            return "Set" + setNumber + ": " + durationMinutes + "min, " + distanceKm + "km}";
        }
        return "Set" + setNumber + ": " + weightKg + "kg x " + reps + " reps}";
    }
}