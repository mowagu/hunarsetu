package com.handymanhub.backend.dto;

import java.time.LocalDateTime;

// A DTO (Data Transfer Object) is what we actually send/receive over
// the API — deliberately kept separate from the @Entity class.
// Why bother with two classes that look almost the same? Because the
// entity is shaped around what the DATABASE needs (JPA annotations,
// lazy relationships), while the DTO is shaped around what the CLIENT
// needs (plain fields, no risk of accidentally serializing an entire
// object graph into an infinite loop).
public class WorkerDto {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;

    public WorkerDto() {
    }

    public WorkerDto(Long id, String name, String email, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
