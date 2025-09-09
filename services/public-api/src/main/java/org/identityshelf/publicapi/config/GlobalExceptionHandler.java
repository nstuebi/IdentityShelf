package org.identityshelf.publicapi.config;

import org.identityshelf.publicapi.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private boolean isSpringDocPath(String path) {
        return path != null && (path.contains("/v3/api-docs") || 
                               path.contains("/swagger-ui") || 
                               path.contains("/swagger-resources") ||
                               path.contains("/webjars") ||
                               path.contains("/api-docs"));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        String path = request.getDescription(false).replace("uri=", "");
        if (isSpringDocPath(path)) {
            return null; // Let SpringDoc handle its own errors
        }
        
        Map<String, Object> details = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            details.put(fieldName, errorMessage);
        });
        
        ErrorResponse errorResponse = new ErrorResponse(
            "VALIDATION_ERROR",
            "Validation failed",
            OffsetDateTime.now(),
            request.getDescription(false).replace("uri=", ""),
            HttpStatus.BAD_REQUEST.value(),
            details
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleSpecificExceptions(
            Exception ex, WebRequest request) {
        
        String path = request.getDescription(false).replace("uri=", "");
        if (isSpringDocPath(path)) {
            return null; // Let SpringDoc handle its own errors
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            "INVALID_ARGUMENT",
            ex.getMessage(),
            OffsetDateTime.now(),
            request.getDescription(false).replace("uri=", ""),
            HttpStatus.BAD_REQUEST.value(),
            null
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
