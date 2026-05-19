package com.hevy.hevy.dto.response;

import com.hevy.hevy.model.WorkoutSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class WorkoutSessionResponse {
    private Long id;
    private Long userId;
    private String notes;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private boolean active;
    private boolean paused;
    private long durationMinutes;
    private List<WorkoutExerciseResponse> exercises;

    public static WorkoutSessionResponse from(WorkoutSession session) {
        WorkoutSessionResponse dto = new WorkoutSessionResponse();
        dto.id = session.getId();
        dto.userId = session.getUserId();
        dto.notes = session.getNotes();
        dto.startedAt = session.getStartedAt();
        dto.finishedAt = session.getFinishedAt();
        dto.active = session.isActive();
        dto.paused = session.isPaused();
        dto.durationMinutes = session.getDurationMinutes();
        dto.exercises = session.getExercises().stream()
                .map(WorkoutExerciseResponse::from)
                .collect(Collectors.toList());
        return dto;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getNotes() { return notes; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public boolean isActive() { return active; }
    public boolean isPaused() { return paused; }
    public long getDurationMinutes() { return durationMinutes; }
    public List<WorkoutExerciseResponse> getExercises() { return exercises; }
}