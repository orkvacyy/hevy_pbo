package com.hevy.hevy.dto.request;

public class FinishSessionRequest {
    private Long userId;
    private String notes;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}