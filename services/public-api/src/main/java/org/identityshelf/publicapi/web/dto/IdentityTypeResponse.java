package org.identityshelf.publicapi.web.dto;

import java.time.OffsetDateTime;
import java.util.List;

public class IdentityTypeResponse {
    private final String id;
    private final String name;
    private final String displayName;
    private final String description;
    private final boolean active;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
    private final List<AttributeTypeResponse> attributes;
    
    public IdentityTypeResponse(
        String id,
        String name,
        String displayName,
        String description,
        boolean active,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        List<AttributeTypeResponse> attributes
    ) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.attributes = attributes;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public List<AttributeTypeResponse> getAttributes() { return attributes; }
}
