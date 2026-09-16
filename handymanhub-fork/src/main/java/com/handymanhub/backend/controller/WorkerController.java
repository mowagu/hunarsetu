package com.handymanhub.backend.controller;

import com.handymanhub.backend.dto.WorkerCreateRequest;
import com.handymanhub.backend.dto.WorkerDto;
import com.handymanhub.backend.service.WorkerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController = @Controller + @ResponseBody: every method's return
// value is automatically converted to JSON and written to the HTTP
// response body, instead of looking for an HTML template to render.
@RestController
// Every endpoint in this class is prefixed with /api/workers.
@RequestMapping("/api/workers")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    // POST /api/workers  — create a worker
    @PostMapping
    public ResponseEntity<WorkerDto> create(@Valid @RequestBody WorkerCreateRequest request) {
        // @Valid triggers the @NotBlank/@Email checks on the DTO before
        // this method body even runs. If validation fails, Spring
        // throws MethodArgumentNotValidException, which
        // GlobalExceptionHandler turns into a 400 response for us.
        WorkerDto created = workerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/workers/{id} — fetch one worker
    @GetMapping("/{id}")
    public WorkerDto getById(@PathVariable Long id) {
        return workerService.getById(id);
    }

    // GET /api/workers — list all workers
    @GetMapping
    public List<WorkerDto> getAll() {
        return workerService.getAll();
    }

    // DELETE /api/workers/{id} — remove a worker
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
