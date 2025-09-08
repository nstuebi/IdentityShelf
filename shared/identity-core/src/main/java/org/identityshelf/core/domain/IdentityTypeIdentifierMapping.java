package org.identityshelf.core.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class IdentityTypeIdentifierMapping {
    private UUID uuid;
    private IdentityType identityType;
    private IdentifierType identifierType;
    private int sortOrder;
    private boolean primaryCandidate;
    private String overrideValidationRegex;
    private String overrideDefaultValue;
    private boolean active;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
    // Constructors
    public IdentityTypeIdentifierMapping() {}
    
    public IdentityTypeIdentifierMapping(UUID uuid, IdentityType identityType, IdentifierType identifierType, 
                                       int sortOrder, boolean primaryCandidate, String overrideValidationRegex, 
                                       String overrideDefaultValue, boolean active, 
                                       OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.uuid = uuid;
        this.identityType = identityType;
        this.identifierType = identifierType;
        this.sortOrder = sortOrder;
        this.primaryCandidate = primaryCandidate;
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
    public IdentifierType getIdentifierType() { return identifierType; }
    public void setIdentifierType(IdentifierType identifierType) { this.identifierType = identifierType; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
    public boolean isPrimaryCandidate() { return primaryCandidate; }
    public void setPrimaryCandidate(boolean primaryCandidate) { this.primaryCandidate = primaryCandidate; }
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
        return overrideValidationRegex != null ? overrideValidationRegex : identifierType.getValidationRegex();
    }
    
    public String getEffectiveDefaultValue() {
        return overrideDefaultValue != null ? overrideDefaultValue : identifierType.getDefaultValue();
    }
}
