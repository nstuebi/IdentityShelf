package org.identityshelf.core.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class AttributeType {
    private UUID uuid;
    private String name;
    private String displayName;
    private String description;
    private AttributeDataType dataType;
    private boolean required;
    private String defaultValue;
    private String validationRegex;
    private int sortOrder;
    private boolean active;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
    // Constructors
    public AttributeType() {}
    
    public AttributeType(UUID uuid, String name, String displayName, String description, 
                        AttributeDataType dataType, boolean required, String defaultValue, 
                        String validationRegex, int sortOrder, boolean active, 
                        OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.uuid = uuid;
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
    
    // Getters and setters
    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public AttributeDataType getDataType() { return dataType; }
    public void setDataType(AttributeDataType dataType) { this.dataType = dataType; }
    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }
    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
    public String getValidationRegex() { return validationRegex; }
    public void setValidationRegex(String validationRegex) { this.validationRegex = validationRegex; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
