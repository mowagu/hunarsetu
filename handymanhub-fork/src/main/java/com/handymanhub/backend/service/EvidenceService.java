package com.handymanhub.backend.service;

import com.handymanhub.backend.dto.EvidenceCreateRequest;
import com.handymanhub.backend.dto.EvidenceDto;
import com.handymanhub.backend.exception.ResourceNotFoundException;
import com.handymanhub.backend.model.Evidence;
import com.handymanhub.backend.model.Mentor;
import com.handymanhub.backend.model.Task;
import com.handymanhub.backend.model.Worker;
import com.handymanhub.backend.repository.EvidenceRepository;
import com.handymanhub.backend.repository.MentorRepository;
import com.handymanhub.backend.repository.TaskRepository;
import com.handymanhub.backend.repository.WorkerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvidenceService {

    private final EvidenceRepository evidenceRepository;
    private final WorkerRepository workerRepository;
    private final MentorRepository mentorRepository;
    private final TaskRepository taskRepository;
    private final ScoringService scoringService;

    public EvidenceService(EvidenceRepository evidenceRepository,
                            WorkerRepository workerRepository,
                            MentorRepository mentorRepository,
                            TaskRepository taskRepository,
                            ScoringService scoringService) {
        this.evidenceRepository = evidenceRepository;
        this.workerRepository = workerRepository;
        this.mentorRepository = mentorRepository;
        this.taskRepository = taskRepository;
        this.scoringService = scoringService;
    }

    public EvidenceDto create(EvidenceCreateRequest request) {
        // Look up each related entity by the ID the client sent us.
        // If any of them don't exist, we stop here with a clear 404 —
        // we never want to save an Evidence row pointing at a worker
        // that doesn't exist (the database's foreign key would reject
        // it anyway, but this gives a nicer error message).
        Worker worker = workerRepository.findById(request.getWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found: " + request.getWorkerId()));
        Mentor mentor = mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found: " + request.getMentorId()));
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + request.getTaskId()));

        Evidence evidence = new Evidence(
                worker,
                mentor,
                task,
                request.getDifficulty(),
                request.getRating(),
                request.getStatus()
        );

        Evidence saved = evidenceRepository.save(evidence);
        return toDto(saved);
    }

    public EvidenceDto getById(Long id) {
        Evidence evidence = evidenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidence not found: " + id));
        return toDto(evidence);
    }

    public List<EvidenceDto> getAll() {
        return evidenceRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public List<EvidenceDto> getByWorkerId(Long workerId) {
        return evidenceRepository.findByWorkerId(workerId).stream()
                .map(this::toDto)
                .toList();
    }

    public void delete(Long id) {
        if (!evidenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evidence not found: " + id);
        }
        evidenceRepository.deleteById(id);
    }

    // Converts an Evidence entity into an EvidenceDto, filling in the
    // computed scoring fields by calling ScoringService. This is the
    // one place those two worlds (persistence + scoring) meet.
    private EvidenceDto toDto(Evidence evidence) {
        EvidenceDto dto = new EvidenceDto();
        dto.setId(evidence.getId());
        dto.setWorkerId(evidence.getWorker().getId());
        dto.setMentorId(evidence.getMentor().getId());
        dto.setTaskId(evidence.getTask().getId());
        dto.setTimestamp(evidence.getTimestamp());
        dto.setDifficulty(evidence.getDifficulty());
        dto.setRating(evidence.getRating());
        dto.setStatus(evidence.getStatus());

        ScoringService.ScoreResult result = scoringService.score(evidence);
        dto.setWeightedScore(result.weightedScore());
        dto.setDecayFactor(result.decayFactor());
        dto.setDecayedScore(result.decayedScore());

        return dto;
    }
}
