package org.identityshelf.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.identityshelf.identity.exception.ValidationException;
import org.identityshelf.identity.service.IdentityValidationService.ValidationError;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    private final ErrorHandlingConfig errorHandlingConfig;
    
    public GlobalExceptionHandler(ErrorHandlingConfig errorHandlingConfig) {
        this.errorHandlingConfig = errorHandlingConfig;
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Unhandled exception occurred", ex);
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("path", request.getDescription(false).replace("uri=", ""));
        
        // Always include basic message
        errorResponse.put("message", ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred");
        
        // Add enhanced details if configured
        if (errorHandlingConfig.isEnhancedDetailEnabled()) {
            errorResponse.put("exception", ex.getClass().getSimpleName());
            
            if (ex.getCause() != null) {
                errorResponse.put("rootCause", ex.getCause().getMessage());
            }
        }
        
        // Add full details including stack trace if configured
        if (errorHandlingConfig.isFullDetailEnabled()) {
            errorResponse.put("exception", ex.getClass().getName());
            errorResponse.put("stackTrace", getStackTraceAsString(ex));
            
            if (ex.getCause() != null) {
                errorResponse.put("rootCause", ex.getCause().getMessage());
                errorResponse.put("rootCauseClass", ex.getCause().getClass().getName());
            }
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex, WebRequest request) {
        logger.warn("Validation exception: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Validation Failed");
        errorResponse.put("path", request.getDescription(false).replace("uri=", ""));
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("validationErrors", ex.getErrors());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.warn("Illegal argument exception: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Bad Request");
        errorResponse.put("path", request.getDescription(false).replace("uri=", ""));
        errorResponse.put("message", ex.getMessage());
        
        if (errorHandlingConfig.isEnhancedDetailEnabled()) {
            errorResponse.put("exception", ex.getClass().getSimpleName());
        }
        
        if (errorHandlingConfig.isFullDetailEnabled()) {
            errorResponse.put("exception", ex.getClass().getName());
            errorResponse.put("stackTrace", getStackTraceAsString(ex));
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex, WebRequest request) {
        logger.error("Runtime exception occurred", ex);
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("path", request.getDescription(false).replace("uri=", ""));
        errorResponse.put("message", ex.getMessage() != null ? ex.getMessage() : "A runtime error occurred");
        
        if (errorHandlingConfig.isEnhancedDetailEnabled()) {
            errorResponse.put("exception", ex.getClass().getSimpleName());
        }
        
        if (errorHandlingConfig.isFullDetailEnabled()) {
            errorResponse.put("exception", ex.getClass().getName());
            errorResponse.put("stackTrace", getStackTraceAsString(ex));
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    
    private String getStackTraceAsString(Exception ex) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        
        // Limit stack trace to first 20 elements to avoid overwhelming response
        int maxElements = Math.min(stackTrace.length, 20);
        for (int i = 0; i < maxElements; i++) {
            sb.append(stackTrace[i].toString()).append("\n");
        }
        
        if (stackTrace.length > maxElements) {
            sb.append("... (").append(stackTrace.length - maxElements).append(" more elements)\n");
        }
        
        return sb.toString();
    }
}

