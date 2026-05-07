package com.hevy.hevy.model;

public class CardioExercise extends BaseExercise {

    private String category = "cardio";
    private double durationMinutes;

    public CardioExercise(Long id, String name, String muscleGroup, String equipment, Long ownerId, double durationMinutes) {
        super(id, name, muscleGroup, equipment, ownerId);
        this.durationMinutes = durationMinutes;
    }

    @Override
    public double calculateVolume(double weight, int reps, int sets) {
        return durationMinutes * sets;
    }

    public String getCategory() { return category; }

    public double getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(double durationMinutes) {
        if (durationMinutes < 0) throw new IllegalArgumentException("Durasi tidak boleh negatif");
        this.durationMinutes = durationMinutes;
    }

    @Override
    public String toString() {
        return "CardioExercise nama='" + getName() + "', durasi=" + durationMinutes + "min}";
    }
}