package com.hevy.hevy.model;

public class WorkoutSet {

    private Long id;
    private Long workoutExerciseId;
    private int setNumber;
    private double weightKg;
    private int reps;

    public WorkoutSet(Long workoutExerciseId, int setNumber, double weightKg, int reps) {
        this.workoutExerciseId = workoutExerciseId;
        this.setNumber         = setNumber;
        setWeightKg(weightKg); // setter biar validasi jalan
        setReps(reps);
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

    @Override
    public String toString() {
        return "Set" + setNumber + ": " + weightKg + "kg x " + reps + " reps}";
    }
}