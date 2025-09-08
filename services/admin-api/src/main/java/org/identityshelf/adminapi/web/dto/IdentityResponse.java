package org.identityshelf.adminapi.web.dto;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public class IdentityResponse {
    private final UUID id;
    private final String displayName;
    private final String status;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
    private final String identityType;
    private final Map<String, Object> attributes;
    
    // Constructor
    public IdentityResponse(UUID id, String displayName, String status, OffsetDateTime createdAt, 
                           OffsetDateTime updatedAt, String identityType, Map<String, Object> attributes) {
        this.id = id;
        this.displayName = displayName;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.identityType = identityType;
        this.attributes = attributes;
    }
    
    // Getters
    public UUID getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getStatus() { return status; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public String getIdentityType() { return identityType; }
    public Map<String, Object> getAttributes() { return attributes; }
}
