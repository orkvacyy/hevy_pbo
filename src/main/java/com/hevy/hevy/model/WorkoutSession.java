package com.hevy.hevy.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WorkoutSession {

    private Long id;
    private Long userId;
    private String notes;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private List<WorkoutExercise> exercises;

    // cons
    public WorkoutSession(Long userId) {
        this.userId    = userId;
        this.startedAt = LocalDateTime.now();
        this.exercises = new ArrayList<>();
    }

    public void start() {
        this.startedAt = LocalDateTime.now();
    }

    public void finish(String notes) {
        this.finishedAt = LocalDateTime.now();
        this.notes      = notes;
    }

    public boolean isActive() {
        return this.finishedAt == null;
    }

    public long getDurationMinutes() {
        if (finishedAt == null) return 0;
        return java.time.Duration.between(startedAt, finishedAt).toMinutes();
    }

    // ── Getter & Setter ────────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }

    public List<WorkoutExercise> getExercises() { return exercises; }
    public void setExercises(List<WorkoutExercise> exercises) { this.exercises = exercises; }

    @Override
    public String toString() {
        return "Workout Session ID = " + id + ", userId = " + userId + ", active = " + isActive() + "";
    }
}