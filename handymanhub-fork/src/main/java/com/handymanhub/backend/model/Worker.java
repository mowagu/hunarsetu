package com.handymanhub.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// @Entity tells Spring/Hibernate: "this Java class maps to a database
// table." By default the table name is the lowercased class name
// ("worker"), which matches the table we created in V1__init_evidence_schema.sql.
@Entity
@Table(name = "worker")
public class Worker {

    @Id
    // IDENTITY means "let the database auto-generate this value" —
    // it matches the BIGSERIAL column type we used in the migration.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // JPA requires a no-argument constructor so it can create instances
    // of this class when loading rows from the database.
    public Worker() {
    }

    public Worker(String name, String email) {
        this.name = name;
        this.email = email;
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
