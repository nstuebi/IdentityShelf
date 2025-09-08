package org.identityshelf.publicapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.Map;

@Schema(description = "Update identity request")
public class UpdateIdentityRequest {
    
    @Schema(description = "Display name", example = "John Doe", maxLength = 255)
    @Size(max = 255, message = "Display name must not exceed 255 characters")
    @JsonProperty("displayName")
    private String displayName;
    
    @Schema(description = "Identity status", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED", "PENDING"})
    @JsonProperty("status")
    private String status;
    
    @Schema(description = "Identity attributes")
    @JsonProperty("attributes")
    private Map<String, Object> attributes;
    
    // Constructors
    public UpdateIdentityRequest() {}
    
    public UpdateIdentityRequest(String displayName, String status, Map<String, Object> attributes) {
        this.displayName = displayName;
        this.status = status;
        this.attributes = attributes;
    }
    
    // Getters and setters
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Map<String, Object> getAttributes() { return attributes; }
    public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }
}