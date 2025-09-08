package org.identityshelf.publicapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Health check response")
public class HealthResponse {
    
    @Schema(description = "Service status", example = "UP", allowableValues = {"UP", "DOWN"})
    @JsonProperty("status")
    private String status;
    
    @Schema(description = "Response timestamp")
    @JsonProperty("timestamp")
    private OffsetDateTime timestamp;
    
    @Schema(description = "API version")
    @JsonProperty("version")
    private String version;
    
    @Schema(description = "Service uptime in seconds")
    @JsonProperty("uptime")
    private Long uptime;
    
    // Constructors
    public HealthResponse() {}
    
    public HealthResponse(String status, OffsetDateTime timestamp, String version, Long uptime) {
        this.status = status;
        this.timestamp = timestamp;
        this.version = version;
        this.uptime = uptime;
    }
    
    // Getters and setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public Long getUptime() { return uptime; }
    public void setUptime(Long uptime) { this.uptime = uptime; }
}
