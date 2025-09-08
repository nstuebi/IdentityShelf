package org.identityshelf.publicapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.Map;

@Schema(description = "Error response")
public class ErrorResponse {
    
    @Schema(description = "Error type", example = "VALIDATION_ERROR")
    @JsonProperty("error")
    private String error;
    
    @Schema(description = "Error message", example = "Validation failed")
    @JsonProperty("message")
    private String message;
    
    @Schema(description = "Error timestamp")
    @JsonProperty("timestamp")
    private OffsetDateTime timestamp;
    
    @Schema(description = "Request path", example = "/v1/identities")
    @JsonProperty("path")
    private String path;
    
    @Schema(description = "HTTP status code", example = "400")
    @JsonProperty("status")
    private Integer status;
    
    @Schema(description = "Additional error details")
    @JsonProperty("details")
    private Map<String, Object> details;
    
    // Constructors
    public ErrorResponse() {}
    
    public ErrorResponse(String error, String message, OffsetDateTime timestamp, 
                        String path, Integer status, Map<String, Object> details) {
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
        this.status = status;
        this.details = details;
    }
    
    // Getters and setters
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    
    public Map<String, Object> getDetails() { return details; }
    public void setDetails(Map<String, Object> details) { this.details = details; }
}
