package com.handymanhub.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Table(name = "evidence")
public class Evidence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Relationships ---
    // @ManyToOne means: many Evidence rows can point at one Worker.
    // @JoinColumn says which database column stores that link
    // (worker_id, matching the foreign key we created in the migration).
    // FetchType.LAZY means: don't load the full Worker object from the
    // database until someone actually calls evidence.getWorker() — this
    // keeps simple queries fast.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "\"timestamp\"", nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    // @Min/@Max here double up the CHECK constraint we put in the SQL
    // migration. Validating in Java gives a friendly error message;
    // the database CHECK is the last line of defense if something
    // bypasses the Java layer entirely.
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private int difficulty;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private int rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EvidenceStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Evidence() {
    }

    public Evidence(Worker worker, Mentor mentor, Task task, int difficulty, int rating, EvidenceStatus status) {
        this.worker = worker;
        this.mentor = mentor;
        this.task = task;
        this.difficulty = difficulty;
        this.rating = rating;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
