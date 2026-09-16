package com.handymanhub.backend.model;

// An enum restricts the "status" field to a fixed set of valid values.
// This is a Java-side safety net on top of the VARCHAR column in the
// database — it stops typos like "Aproved" from ever being written.
public enum EvidenceStatus {
    SUBMITTED,
    APPROVED,
    REJECTED
}
