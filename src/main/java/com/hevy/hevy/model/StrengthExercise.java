package com.hevy.hevy.model;

public class StrengthExercise extends BaseExercise {

    private String category = "strength";

    public StrengthExercise(Long id, String name, String muscleGroup, String equipment, Long ownerId) {
        super(id, name, muscleGroup, equipment, ownerId);
    }

    @Override
    public double calculateVolume(double weight, int reps, int sets) {
        return weight * reps * sets;
    }

    public String getCategory() { return category; }

    @Override
    public String toString() {
        return "StrengthExercise{name='" + getName() + "', muscle=" + getMuscleGroup() + "}";
    }
}