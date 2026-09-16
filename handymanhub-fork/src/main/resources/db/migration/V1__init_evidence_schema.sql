-- ============================================================================
-- V1__init_evidence_schema.sql
--
-- Flyway rule: once a migration file has been run against a database,
-- you never edit it again. If you need a schema change later, you add a
-- NEW file: V2__something.sql, V3__something_else.sql, and so on. Flyway
-- keeps a table (flyway_schema_history) that records which files it has
-- already run, so it never re-runs V1 twice.
--
-- This file does two things, in order:
--   1. Drops the old handymanhub tables (skills, contractors, workers,
--      bookings) since we're repurposing this project for a different
--      domain.
--   2. Creates the four new tables: worker, mentor, task, evidence.
-- ============================================================================

-- --- 1. Remove the old schema -------------------------------------------
-- IF EXISTS means this won't error out even if a table was already removed
-- or never existed on a fresh database. CASCADE also drops anything that
-- depends on that table (like foreign keys pointing at it).
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS workers CASCADE;
DROP TABLE IF EXISTS contractors CASCADE;
DROP TABLE IF EXISTS skills CASCADE;

-- --- 2. Create the new schema --------------------------------------------

-- A Worker is a person doing tasks and submitting evidence of that work.
CREATE TABLE worker (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- A Mentor reviews a worker's evidence and rates it.
CREATE TABLE mentor (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- A Task is a unit of work a worker can be assigned/attempt.
CREATE TABLE task (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);

-- Evidence links a worker + mentor + task together: it's the record of
-- "this worker did this task, this mentor reviewed it, here's the result."
CREATE TABLE evidence (
    id          BIGSERIAL PRIMARY KEY,

    -- Foreign keys: each of these MUST point to a row that actually
    -- exists in the referenced table. The database enforces this for us
    -- so we can never end up with an evidence row pointing at a worker
    -- that doesn't exist.
    worker_id   BIGINT NOT NULL REFERENCES worker(id) ON DELETE CASCADE,
    mentor_id   BIGINT NOT NULL REFERENCES mentor(id) ON DELETE CASCADE,
    task_id     BIGINT NOT NULL REFERENCES task(id)   ON DELETE CASCADE,

    "timestamp" TIMESTAMP NOT NULL DEFAULT now(),

    -- CHECK constraints: the database itself refuses to store a row
    -- that violates these, as a second line of defense beyond our Java
    -- validation.
    difficulty  INT NOT NULL CHECK (difficulty BETWEEN 1 AND 5),
    rating      INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    status      VARCHAR(50) NOT NULL,

    created_at  TIMESTAMP NOT NULL DEFAULT now()
);

-- Indexes on the foreign key columns speed up the common queries:
-- "all evidence for this worker", "all evidence this mentor reviewed", etc.
CREATE INDEX idx_evidence_worker_id ON evidence(worker_id);
CREATE INDEX idx_evidence_mentor_id ON evidence(mentor_id);
CREATE INDEX idx_evidence_task_id   ON evidence(task_id);
