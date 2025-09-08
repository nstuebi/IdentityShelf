package org.identityshelf.adminapi.web.dto;

import org.identityshelf.core.domain.IdentifierType;

import java.time.OffsetDateTime;

public class IdentifierTypeResponse {
    private final String id;
    private final String name;
    private final String displayName;
    private final String description;
    private final String dataType;
    private final String validationRegex;
    private final String defaultValue;
    private final boolean unique;
    private final boolean searchable;
    private final boolean active;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    // Constructor
    public IdentifierTypeResponse(String id, String name, String displayName, String description,
                                 String dataType, String validationRegex, String defaultValue,
                                 boolean unique, boolean searchable, boolean active,
                                 OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.dataType = dataType;
        this.validationRegex = validationRegex;
        this.defaultValue = defaultValue;
        this.unique = unique;
        this.searchable = searchable;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public static IdentifierTypeResponse from(IdentifierType identifierType) {
        return new IdentifierTypeResponse(
                identifierType.getUuid().toString(),
                identifierType.getName(),
                identifierType.getDisplayName(),
                identifierType.getDescription(),
                identifierType.getDataType().name(),
                identifierType.getValidationRegex(),
                identifierType.getDefaultValue(),
                identifierType.isUnique(),
                identifierType.isSearchable(),
                identifierType.isActive(),
                identifierType.getCreatedAt(),
                identifierType.getUpdatedAt()
        );
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public String getDataType() { return dataType; }
    public String getValidationRegex() { return validationRegex; }
    public String getDefaultValue() { return defaultValue; }
    public boolean isUnique() { return unique; }
    public boolean isSearchable() { return searchable; }
    public boolean isActive() { return active; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
