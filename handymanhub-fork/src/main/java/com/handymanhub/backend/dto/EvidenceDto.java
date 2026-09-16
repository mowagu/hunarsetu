package com.handymanhub.backend.dto;

import com.handymanhub.backend.model.EvidenceStatus;
import java.time.LocalDateTime;

// This is what we send BACK to the client when they ask for evidence —
// it includes the computed scoring fields too, so the client never has
// to re-implement the math itself.
public class EvidenceDto {
    private Long id;
    private Long workerId;
    private Long mentorId;
    private Long taskId;
    private LocalDateTime timestamp;
    private int difficulty;
    private int rating;
    private EvidenceStatus status;

    // Computed, not stored: the result of ScoringService's math for
    // this particular evidence row.
    private double weightedScore;
    private double decayFactor;
    private double decayedScore;

    public EvidenceDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public EvidenceStatus getStatus() {
        return status;
    }

    public void setStatus(EvidenceStatus status) {
        this.status = status;
    }

    public double getWeightedScore() {
        return weightedScore;
    }

    public void setWeightedScore(double weightedScore) {
        this.weightedScore = weightedScore;
    }

    public double getDecayFactor() {
        return decayFactor;
    }

    public void setDecayFactor(double decayFactor) {
        this.decayFactor = decayFactor;
    }

    public double getDecayedScore() {
        return decayedScore;
    }

    public void setDecayedScore(double decayedScore) {
        this.decayedScore = decayedScore;
    }
}
