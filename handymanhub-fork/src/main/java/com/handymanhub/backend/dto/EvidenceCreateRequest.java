package com.handymanhub.backend.dto;

import com.handymanhub.backend.model.EvidenceStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// What the client sends us to CREATE a new evidence record. Notice we
// take workerId/mentorId/taskId (plain numbers) here, not Worker/Mentor
// /Task objects — the service layer is responsible for looking up the
// real entities from those IDs.
public class EvidenceCreateRequest {

    @NotNull(message = "workerId is required")
    private Long workerId;

    @NotNull(message = "mentorId is required")
    private Long mentorId;

    @NotNull(message = "taskId is required")
    private Long taskId;

    @NotNull(message = "difficulty is required")
    @Min(value = 1, message = "difficulty must be between 1 and 5")
    @Max(value = 5, message = "difficulty must be between 1 and 5")
    private Integer difficulty;

    @NotNull(message = "rating is required")
    @Min(value = 1, message = "rating must be between 1 and 5")
    @Max(value = 5, message = "rating must be between 1 and 5")
    private Integer rating;

    @NotNull(message = "status is required")
    private EvidenceStatus status;

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public EvidenceStatus getStatus() {
        return status;
    }

    public void setStatus(EvidenceStatus status) {
        this.status = status;
    }
}
