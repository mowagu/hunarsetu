package com.handymanhub.backend.service;

import com.handymanhub.backend.dto.MentorCreateRequest;
import com.handymanhub.backend.dto.MentorDto;
import com.handymanhub.backend.exception.ResourceNotFoundException;
import com.handymanhub.backend.model.Mentor;
import com.handymanhub.backend.repository.MentorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentorService {

    private final MentorRepository mentorRepository;

    public MentorService(MentorRepository mentorRepository) {
        this.mentorRepository = mentorRepository;
    }

    public MentorDto create(MentorCreateRequest request) {
        Mentor mentor = new Mentor(request.getName(), request.getEmail());
        Mentor saved = mentorRepository.save(mentor);
        return toDto(saved);
    }

    public MentorDto getById(Long id) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found: " + id));
        return toDto(mentor);
    }

    public List<MentorDto> getAll() {
        return mentorRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public void delete(Long id) {
        if (!mentorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mentor not found: " + id);
        }
        mentorRepository.deleteById(id);
    }

    private MentorDto toDto(Mentor mentor) {
        return new MentorDto(mentor.getId(), mentor.getName(), mentor.getEmail(), mentor.getCreatedAt());
    }
}
