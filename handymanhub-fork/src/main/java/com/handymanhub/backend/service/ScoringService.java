package com.handymanhub.backend.service;

import com.handymanhub.backend.model.Evidence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

// This service holds the two math functions you asked for:
//   1. Difficulty weighting
//   2. Recency decay
//
// It's a separate class (not stuffed into EvidenceService) on purpose:
// "how do we score evidence" is a distinct responsibility from "how do
// we save/fetch evidence from the database", and keeping them apart
// makes each easier to read, test, and change independently.
@Service
public class ScoringService {

    // The decay "speed". A bigger lambda means old evidence loses value
    // FASTER. We default it to 0.1, but pull it from
    // application.properties (key: scoring.recency-decay-lambda) so you
    // can tune it without touching code. If that property isn't set,
    // Spring falls back to the 0.1 after the colon.
    @Value("${scoring.recency-decay-lambda:0.1}")
    private double defaultLambda;

    // =========================================================================
    // FUNCTION 1: Difficulty weighting
    // =========================================================================
    //
    // The idea: a 5-star rating on an easy task (difficulty 1) shouldn't
    // count as much as a 5-star rating on a hard task (difficulty 5).
    // So we multiply the raw rating by the difficulty to get a
    // "weighted score" that rewards harder work more.
    //
    // Step by step:
    //   Step 1: take the raw rating a mentor gave (1-5)
    //   Step 2: take the difficulty of the task (1-5)
    //   Step 3: multiply them together
    //
    // Example: rating = 4, difficulty = 5  ->  weightedScore = 20
    //          rating = 4, difficulty = 1  ->  weightedScore = 4
    // Same rating, very different weighted score, because difficulty
    // acts as a multiplier on top of the rating.
    public double applyDifficultyWeighting(int rating, int difficulty) {
        // Step 1 & 2 are just the method's two input parameters.
        // Step 3: multiply. Cast to double so the result can hold
        // fractional values later if you change this formula.
        return (double) rating * (double) difficulty;
    }

    // Convenience overload that pulls rating/difficulty straight off an
    // Evidence entity, so callers don't have to unpack the fields
    // themselves every time.
    public double applyDifficultyWeighting(Evidence evidence) {
        return applyDifficultyWeighting(evidence.getRating(), evidence.getDifficulty());
    }

    // =========================================================================
    // FUNCTION 2: Recency decay
    // =========================================================================
    //
    // The idea: evidence from a year ago should matter less right now
    // than evidence from yesterday. We model that "fading" with
    // exponential decay — the same math used for radioactive decay or
    // cooling coffee.
    //
    // Formula:  decay = e^(-lambda * time_elapsed)
    //
    //   - "e" is Euler's number (~2.718), a math constant.
    //   - "lambda" controls how FAST old evidence fades. Bigger lambda
    //     = faster fade.
    //   - "time_elapsed" is how much time has passed since the evidence
    //     was recorded (we measure it in days here).
    //
    // What the output means:
    //   - time_elapsed = 0 (evidence from right now)  -> decay = 1.0
    //     (no fading at all yet — full value)
    //   - as time_elapsed grows, decay shrinks toward 0 but NEVER
    //     reaches exactly 0 (that's just how exponential decay works)
    //
    // Step by step:
    //   Step 1: figure out how many days have passed since the
    //           evidence's timestamp
    //   Step 2: multiply that by lambda
    //   Step 3: negate it (flip the sign)
    //   Step 4: raise e to that power using Math.exp(...)
    public double computeRecencyDecay(LocalDateTime evidenceTimestamp, double lambda) {
        // Step 1: Duration.between gives us the elapsed time as hours/
        // minutes/seconds; toHours() / 24.0 converts that into days,
        // keeping the fractional part (e.g. 1.5 days), which is more
        // accurate than rounding to whole days.
        long elapsedHours = Duration.between(evidenceTimestamp, LocalDateTime.now()).toHours();
        double timeElapsedInDays = elapsedHours / 24.0;

        // Guard: if the timestamp is somehow in the future, treat
        // elapsed time as 0 instead of a negative number (which would
        // make decay come out GREATER than 1, which doesn't make sense
        // for this formula).
        if (timeElapsedInDays < 0) {
            timeElapsedInDays = 0;
        }

        // Steps 2-4, all in one line:
        //   -lambda * timeElapsedInDays   (steps 2 and 3 combined)
        //   Math.exp(...)                 (step 4: e raised to that power)
        return Math.exp(-lambda * timeElapsedInDays);
    }

    // Overload using the configured default lambda instead of asking
    // the caller to supply one every time.
    public double computeRecencyDecay(LocalDateTime evidenceTimestamp) {
        return computeRecencyDecay(evidenceTimestamp, defaultLambda);
    }

    // =========================================================================
    // Putting both functions together
    // =========================================================================
    //
    // The final score for a piece of evidence is:
    //   decayedScore = weightedScore * decayFactor
    //
    // In words: "start with how much this piece of work was worth
    // (rating x difficulty), then shrink that value based on how long
    // ago it happened."
    public ScoreResult score(Evidence evidence, double lambda) {
        double weightedScore = applyDifficultyWeighting(evidence);
        double decayFactor = computeRecencyDecay(evidence.getTimestamp(), lambda);
        double decayedScore = weightedScore * decayFactor;
        return new ScoreResult(weightedScore, decayFactor, decayedScore);
    }

    public ScoreResult score(Evidence evidence) {
        return score(evidence, defaultLambda);
    }

    // A tiny holder for the three numbers we compute, so callers get
    // all of them back from one method call instead of three.
    public record ScoreResult(double weightedScore, double decayFactor, double decayedScore) {
    }
}
