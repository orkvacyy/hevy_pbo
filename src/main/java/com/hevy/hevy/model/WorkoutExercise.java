package com.hevy.hevy.model;

import java.util.ArrayList;
import java.util.List;

public class WorkoutExercise {

    private Long id;
    private Long sessionId;
    private Long exerciseId;
    private String exerciseNameSnapshot;
    private int orderIndex;
    private List<WorkoutSet> sets;

    public WorkoutExercise(Long sessionId, Long exerciseId, String exerciseNameSnapshot, int orderIndex) {
        this.sessionId             = sessionId;
        this.exerciseId            = exerciseId;
        this.exerciseNameSnapshot  = exerciseNameSnapshot;
        this.orderIndex            = orderIndex;
        this.sets                  = new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public Long getExerciseId() { return exerciseId; }
    public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }

    public String getExerciseNameSnapshot() { return exerciseNameSnapshot; }
    public void setExerciseNameSnapshot(String exerciseNameSnapshot) { this.exerciseNameSnapshot = exerciseNameSnapshot; }

    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }

    public List<WorkoutSet> getSets() { return sets; }
    public void setSets(List<WorkoutSet> sets) { this.sets = sets; }

    @Override
    public String toString() {
        return "Workout exercise = '" + exerciseNameSnapshot + "', sets = " + sets.size();
    }
}