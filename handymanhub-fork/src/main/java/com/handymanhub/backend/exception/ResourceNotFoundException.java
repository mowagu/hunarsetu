package com.handymanhub.backend.exception;

// A custom, unchecked exception we throw whenever a lookup by ID fails
// (e.g. someone asks for Worker #999 and it doesn't exist). Having our
// own exception type — instead of throwing a generic RuntimeException —
// lets GlobalExceptionHandler recognize it specifically and turn it
// into a clean 404 response instead of a scary 500 stack trace.
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
