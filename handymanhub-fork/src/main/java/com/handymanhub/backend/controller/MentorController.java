package com.handymanhub.backend.controller;

import com.handymanhub.backend.dto.MentorCreateRequest;
import com.handymanhub.backend.dto.MentorDto;
import com.handymanhub.backend.service.MentorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentors")
public class MentorController {

    private final MentorService mentorService;

    public MentorController(MentorService mentorService) {
        this.mentorService = mentorService;
    }

    @PostMapping
    public ResponseEntity<MentorDto> create(@Valid @RequestBody MentorCreateRequest request) {
        MentorDto created = mentorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public MentorDto getById(@PathVariable Long id) {
        return mentorService.getById(id);
    }

    @GetMapping
    public List<MentorDto> getAll() {
        return mentorService.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mentorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
