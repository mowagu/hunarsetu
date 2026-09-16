package com.handymanhub.backend.service;

import com.handymanhub.backend.dto.WorkerCreateRequest;
import com.handymanhub.backend.dto.WorkerDto;
import com.handymanhub.backend.exception.ResourceNotFoundException;
import com.handymanhub.backend.model.Worker;
import com.handymanhub.backend.repository.WorkerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// @Service marks this as a "business logic" bean. Controllers call
// into services; services call into repositories. Controllers should
// never talk to a Repository directly — that's the whole point of the
// layered structure.
@Service
public class WorkerService {

    private final WorkerRepository workerRepository;

    // Constructor injection: Spring sees this constructor, notices it
    // needs a WorkerRepository, and automatically hands it one (the
    // same bean Spring created from the interface in the repository
    // package). We never call "new WorkerService(...)" ourselves.
    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    public WorkerDto create(WorkerCreateRequest request) {
        Worker worker = new Worker(request.getName(), request.getEmail());
        Worker saved = workerRepository.save(worker);
        return toDto(saved);
    }

    public WorkerDto getById(Long id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found: " + id));
        return toDto(worker);
    }

    public List<WorkerDto> getAll() {
        return workerRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public void delete(Long id) {
        if (!workerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Worker not found: " + id);
        }
        workerRepository.deleteById(id);
    }

    // A small private helper that converts our internal Entity into the
    // DTO shape we expose over the API. Keeping this in one place means
    // we only have to update it once if WorkerDto's shape ever changes.
    private WorkerDto toDto(Worker worker) {
        return new WorkerDto(worker.getId(), worker.getName(), worker.getEmail(), worker.getCreatedAt());
    }
}
