package org.identityshelf.publicapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Schema(description = "Identity response")
public class IdentityResponse {
    
    @Schema(description = "Identity ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonProperty("id")
    private UUID id;
    
    @Schema(description = "Display name", example = "John Doe")
    @JsonProperty("displayName")
    private String displayName;
    
    @Schema(description = "Identity status", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED", "PENDING"})
    @JsonProperty("status")
    private String status;
    
    @Schema(description = "Identity type", example = "PERSON")
    @JsonProperty("type")
    private String type;
    
    @Schema(description = "Creation timestamp")
    @JsonProperty("createdAt")
    private OffsetDateTime createdAt;
    
    @Schema(description = "Last update timestamp")
    @JsonProperty("updatedAt")
    private OffsetDateTime updatedAt;
    
    @Schema(description = "Identity attributes")
    @JsonProperty("attributes")
    private Map<String, Object> attributes;
    
    @Schema(description = "Identity identifiers")
    @JsonProperty("identifiers")
    private List<IdentifierResponse> identifiers;
    
    // Constructors
    public IdentityResponse() {}
    
    public IdentityResponse(UUID id, String displayName, String status, String type, 
                           OffsetDateTime createdAt, OffsetDateTime updatedAt, 
                           Map<String, Object> attributes, List<IdentifierResponse> identifiers) {
        this.id = id;
        this.displayName = displayName;
        this.status = status;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.attributes = attributes;
        this.identifiers = identifiers;
    }
    
    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Map<String, Object> getAttributes() { return attributes; }
    public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }
    
    public List<IdentifierResponse> getIdentifiers() { return identifiers; }
    public void setIdentifiers(List<IdentifierResponse> identifiers) { this.identifiers = identifiers; }
}