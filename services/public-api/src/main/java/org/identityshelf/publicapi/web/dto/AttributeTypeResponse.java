package org.identityshelf.publicapi.web.dto;

import java.time.OffsetDateTime;

public class AttributeTypeResponse {
    private final String id;
    private final String name;
    private final String displayName;
    private final String description;
    private final String dataType;
    private final boolean required;
    private final String defaultValue;
    private final String validationRegex;
    private final int sortOrder;
    private final boolean active;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
    
    public AttributeTypeResponse(
        String id,
        String name,
        String displayName,
        String description,
        String dataType,
        boolean required,
        String defaultValue,
        String validationRegex,
        int sortOrder,
        boolean active,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.dataType = dataType;
        this.required = required;
        this.defaultValue = defaultValue;
        this.validationRegex = validationRegex;
        this.sortOrder = sortOrder;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public String getDataType() { return dataType; }
    public boolean isRequired() { return required; }
    public String getDefaultValue() { return defaultValue; }
    public String getValidationRegex() { return validationRegex; }
    public int getSortOrder() { return sortOrder; }
    public boolean isActive() { return active; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
