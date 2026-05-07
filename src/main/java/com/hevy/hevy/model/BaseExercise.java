package com.hevy.hevy.model;

public abstract class BaseExercise {

//mod 3 encapsulation
    private Long id;
    private String name;
    private String muscleGroup;
    private String equipment;
    private Long ownerId
    private boolean deleted;

    // constructor
    public BaseExercise(Long id, String name, String muscleGroup, String equipment, Long ownerId) {
        this.id          = id;
        this.name        = name;
        this.muscleGroup = muscleGroup;
        this.equipment   = equipment;
        this.ownerId     = ownerId;
        this.deleted     = false;
    }

    public abstract double calculateVolume(double weight, int reps, int sets);

    // manual getter setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMuscleGroup() { return muscleGroup; }
    public void setMuscleGroup(String muscleGroup) { this.muscleGroup = muscleGroup; }

    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    // null = global, != null = local
    public boolean isGlobal() {
        return this.ownerId == null;
    }

    @Override
    public String toString() {
        return "Exercise{id=" + id + ", name='" + name + "', muscle=" + muscleGroup + "}";
    }
}