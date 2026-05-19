package com.hevy.hevy.dto.response;

import com.hevy.hevy.model.WorkoutExercise;

import java.util.List;
import java.util.stream.Collectors;

public class WorkoutExerciseResponse {
    private Long id;
    private Long sessionId;
    private Long exerciseId;
    private String exerciseNameSnapshot;
    private int orderIndex;
    private List<WorkoutSetResponse> sets;

    public static WorkoutExerciseResponse from(WorkoutExercise we) {
        WorkoutExerciseResponse dto = new WorkoutExerciseResponse();
        dto.id = we.getId();
        dto.sessionId = we.getSessionId();
        dto.exerciseId = we.getExerciseId();
        dto.exerciseNameSnapshot = we.getExerciseNameSnapshot();
        dto.orderIndex = we.getOrderIndex();
        dto.sets = we.getSets().stream()
                .map(WorkoutSetResponse::from)
                .collect(Collectors.toList());
        return dto;
    }

    public Long getId() { return id; }
    public Long getSessionId() { return sessionId; }
    public Long getExerciseId() { return exerciseId; }
    public String getExerciseNameSnapshot() { return exerciseNameSnapshot; }
    public int getOrderIndex() { return orderIndex; }
    public List<WorkoutSetResponse> getSets() { return sets; }
}