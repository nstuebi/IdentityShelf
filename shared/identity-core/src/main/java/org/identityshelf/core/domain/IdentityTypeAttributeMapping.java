package org.identityshelf.core.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class IdentityTypeAttributeMapping {
    private UUID uuid;
    private IdentityType identityType;
    private AttributeType attributeType;
    private int sortOrder;
    private String overrideValidationRegex;
    private String overrideDefaultValue;
    private boolean active;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
    // Constructors
    public IdentityTypeAttributeMapping() {}
    
    public IdentityTypeAttributeMapping(UUID uuid, IdentityType identityType, AttributeType attributeType, 
                                      int sortOrder, String overrideValidationRegex, String overrideDefaultValue, 
                                      boolean active, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.uuid = uuid;
        this.identityType = identityType;
        this.attributeType = attributeType;
        this.sortOrder = sortOrder;
        this.overrideValidationRegex = overrideValidationRegex;
        this.overrideDefaultValue = overrideDefaultValue;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and setters
    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }
    public IdentityType getIdentityType() { return identityType; }
    public void setIdentityType(IdentityType identityType) { this.identityType = identityType; }
    public AttributeType getAttributeType() { return attributeType; }
    public void setAttributeType(AttributeType attributeType) { this.attributeType = attributeType; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
    public String getOverrideValidationRegex() { return overrideValidationRegex; }
    public void setOverrideValidationRegex(String overrideValidationRegex) { this.overrideValidationRegex = overrideValidationRegex; }
    public String getOverrideDefaultValue() { return overrideDefaultValue; }
    public void setOverrideDefaultValue(String overrideDefaultValue) { this.overrideDefaultValue = overrideDefaultValue; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Business methods
    public String getEffectiveValidationRegex() {
        return overrideValidationRegex != null ? overrideValidationRegex : attributeType.getValidationRegex();
    }
    
    public String getEffectiveDefaultValue() {
        return overrideDefaultValue != null ? overrideDefaultValue : attributeType.getDefaultValue();
    }
}
