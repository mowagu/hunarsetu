package com.handymanhub.backend.service;

import com.handymanhub.backend.model.Evidence;
import com.handymanhub.backend.model.EvidenceStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

// A plain JUnit test — no Spring, no database needed, because
// ScoringService is pure math with no dependencies. This is exactly
// the kind of logic that's easy and fast to unit test.
class ScoringServiceTest {

    private final ScoringService scoringService = new ScoringService();

    @Test
    void difficultyWeighting_multipliesRatingByDifficulty() {
        double result = scoringService.applyDifficultyWeighting(4, 5);
        assertEquals(20.0, result, 0.0001);
    }

    @Test
    void recencyDecay_isOneWhenTimestampIsNow() {
        double decay = scoringService.computeRecencyDecay(LocalDateTime.now(), 0.1);
        // e^0 = 1, so evidence from right now should have (almost) no decay yet.
        assertEquals(1.0, decay, 0.01);
    }

    @Test
    void recencyDecay_shrinksAsTimeElapses() {
        LocalDateTime tenDaysAgo = LocalDateTime.now().minusDays(10);
        double decay = scoringService.computeRecencyDecay(tenDaysAgo, 0.1);
        // e^(-0.1 * 10) = e^-1 ≈ 0.3679
        assertEquals(0.3679, decay, 0.001);
    }

    @Test
    void score_combinesWeightingAndDecay() {
        Evidence evidence = new Evidence();
        evidence.setRating(4);
        evidence.setDifficulty(5);
        evidence.setTimestamp(LocalDateTime.now().minusDays(10));
        evidence.setStatus(EvidenceStatus.APPROVED);

        ScoringService.ScoreResult result = scoringService.score(evidence, 0.1);

        assertEquals(20.0, result.weightedScore(), 0.0001);
        assertEquals(0.3679, result.decayFactor(), 0.001);
        assertEquals(20.0 * 0.3679, result.decayedScore(), 0.01);
    }
}
