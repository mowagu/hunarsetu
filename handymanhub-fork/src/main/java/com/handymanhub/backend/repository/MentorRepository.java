package com.handymanhub.backend.repository;

import com.handymanhub.backend.model.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorRepository extends JpaRepository<Mentor, Long> {
}
