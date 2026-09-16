package com.handymanhub.backend.repository;

import com.handymanhub.backend.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

// Extending JpaRepository<Worker, Long> gives us save(), findById(),
// findAll(), deleteById(), and more — all fully implemented, with ZERO
// code written by us. Spring generates the implementation at startup.
// The two generic types are: <the entity type, the type of its @Id field>.
public interface WorkerRepository extends JpaRepository<Worker, Long> {
}
