package com.handymanhub.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

// @RestControllerAdvice means: these @ExceptionHandler methods apply to
// EVERY controller in the app. Instead of wrapping every controller
// method in its own try/catch, we throw exceptions from the service
// layer and let this one class translate them into HTTP responses.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Fires whenever any controller method throws ResourceNotFoundException.
    // Turns it into: 404 Not Found + a JSON body explaining why.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
        ApiError body = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // Fires automatically whenever a @Valid-annotated request body
    // fails validation (e.g. difficulty = 9, which violates @Max(5)).
    // Turns it into: 400 Bad Request + a list of every field that failed.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<String> messages = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        ApiError body = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                messages
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
