# handymanhub-fork → Worker/Mentor/Task/Evidence tracker

This project reuses the layered structure and Flyway migration approach
from `shresthbhargava/handymanhub`, repointed at a new domain: workers
submit evidence of completed tasks, mentors review it, and each piece
of evidence gets a computed score.

## How to actually fork the original repo and put this code on GitHub

I can't create a GitHub fork or push commits for you directly — I have
no GitHub access in this session. Here's the manual version, which
only takes a few minutes:

1. Go to `https://github.com/shresthbhargava/handymanhub` and click
   **Fork** (top right). This creates `your-username/handymanhub` in
   your own account.
2. Clone YOUR fork to your computer:
   ```bash
   git clone https://github.com/YOUR_USERNAME/handymanhub.git
   cd handymanhub
   ```
3. Delete the old source and migration files (the ones for skills/
   contractors/workers/bookings), keeping the top-level project files
   (`pom.xml`, `.gitignore`, etc.) as a starting point.
4. Copy every file from this project into that folder, matching the
   same relative paths (`src/main/java/...`, `src/main/resources/...`).
5. Commit and push:
   ```bash
   git add .
   git commit -m "Replace domain with Worker/Mentor/Task/Evidence"
   git push
   ```

## Project layout

```
src/main/java/com/handymanhub/backend/
├── controller/   HTTP endpoints (translates JSON ↔ method calls)
├── service/      Business logic — including ScoringService
├── repository/   Database access (Spring Data JPA interfaces)
├── model/        @Entity classes — map directly to database tables
├── dto/          What actually goes over the API (never the entities directly)
└── exception/    Custom exceptions + a global handler that turns them into clean HTTP errors

src/main/resources/db/migration/
└── V1__init_evidence_schema.sql   The one Flyway migration: drops old tables, creates new ones
```

## Running it

1. Have a PostgreSQL server running locally, with a database named
   `handymanhub` (or edit `application.properties` to match yours).
2. `./mvnw spring-boot:run` (or run `HandymanhubApplication.main()`
   from your IDE).
3. On startup, Flyway automatically runs `V1__init_evidence_schema.sql`
   against your database — you don't run SQL by hand.

## Trying the API

```bash
# Create a worker
curl -X POST localhost:8080/api/workers \
  -H "Content-Type: application/json" \
  -d '{"name":"Asha","email":"asha@example.com"}'

# Create a mentor
curl -X POST localhost:8080/api/mentors \
  -H "Content-Type: application/json" \
  -d '{"name":"Raj","email":"raj@example.com"}'

# Create a task
curl -X POST localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Fix leaking pipe","description":"Kitchen sink"}'

# Submit evidence (use the real ids returned above)
curl -X POST localhost:8080/api/evidence \
  -H "Content-Type: application/json" \
  -d '{"workerId":1,"mentorId":1,"taskId":1,"difficulty":5,"rating":4,"status":"APPROVED"}'
```

The response to that last call includes `weightedScore`, `decayFactor`,
and `decayedScore` — computed live by `ScoringService`, not stored in
the database.

## The two scoring functions, in plain terms

**Difficulty weighting** (`ScoringService.applyDifficultyWeighting`)
`weightedScore = rating * difficulty`. A 4-star rating on a
difficulty-5 task (score 20) counts for more than a 4-star rating on a
difficulty-1 task (score 4) — harder work is worth more.

**Recency decay** (`ScoringService.computeRecencyDecay`)
`decay = e^(-lambda * daysElapsed)`. Fresh evidence (0 days old) has a
decay factor of 1.0 (full value). As days pass, the factor shrinks
toward 0 — old evidence gradually counts for less. `lambda` controls
how fast that fade happens; it's configurable via
`scoring.recency-decay-lambda` in `application.properties` (defaults
to `0.1`).

The two combine as: `decayedScore = weightedScore * decay`.
