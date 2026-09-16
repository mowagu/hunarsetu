package com.handymanhub.backend.repository;

import com.handymanhub.backend.model.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenceRepository extends JpaRepository<Evidence, Long> {

    // Spring Data JPA reads this method NAME and generates the SQL for
    // it automatically — "findByWorkerId" becomes
    // "SELECT * FROM evidence WHERE worker_id = ?". No method body needed.
    List<Evidence> findByWorkerId(Long workerId);

    List<Evidence> findByMentorId(Long mentorId);

    List<Evidence> findByTaskId(Long taskId);
}
