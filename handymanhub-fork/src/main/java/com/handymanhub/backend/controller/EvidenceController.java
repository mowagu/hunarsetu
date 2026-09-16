package com.handymanhub.backend.controller;

import com.handymanhub.backend.dto.EvidenceCreateRequest;
import com.handymanhub.backend.dto.EvidenceDto;
import com.handymanhub.backend.service.EvidenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evidence")
public class EvidenceController {

    private final EvidenceService evidenceService;

    public EvidenceController(EvidenceService evidenceService) {
        this.evidenceService = evidenceService;
    }

    // POST /api/evidence — submit a new piece of evidence.
    // The response includes weightedScore/decayFactor/decayedScore,
    // computed fresh by ScoringService every time this is called.
    @PostMapping
    public ResponseEntity<EvidenceDto> create(@Valid @RequestBody EvidenceCreateRequest request) {
        EvidenceDto created = evidenceService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public EvidenceDto getById(@PathVariable Long id) {
        return evidenceService.getById(id);
    }

    @GetMapping
    public List<EvidenceDto> getAll() {
        return evidenceService.getAll();
    }

    // GET /api/evidence/worker/{workerId} — all evidence for one worker,
    // each with its own freshly-computed decayed score (since decay
    // depends on "now", scores here shift a little every day).
    @GetMapping("/worker/{workerId}")
    public List<EvidenceDto> getByWorker(@PathVariable Long workerId) {
        return evidenceService.getByWorkerId(workerId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        evidenceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
