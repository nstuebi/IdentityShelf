package org.identityshelf.adminapi.web.dto;

import org.identityshelf.core.domain.IdentityTypeIdentifierMapping;

import java.time.OffsetDateTime;

public class IdentityTypeIdentifierMappingResponse {
    private String id;
    private String identityTypeId;
    private String identityTypeName;
    private String identifierTypeId;
    private String identifierTypeName;
    private String identifierTypeDisplayName;
    private String identifierTypeDescription;
    private String identifierDataType;
    private int sortOrder;
    private boolean required;
    private boolean primaryCandidate;
    private String overrideValidationRegex;
    private String overrideDefaultValue;
    private boolean active;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String effectiveValidationRegex;
    private String effectiveDefaultValue;
    private String baseValidationRegex;
    private String baseDefaultValue;

    public static IdentityTypeIdentifierMappingResponse from(IdentityTypeIdentifierMapping mapping) {
        return new IdentityTypeIdentifierMappingResponse(
                mapping.getUuid().toString(),
                mapping.getIdentityType().getUuid().toString(),
                mapping.getIdentityType().getName(),
                mapping.getIdentifierType().getUuid().toString(),
                mapping.getIdentifierType().getName(),
                mapping.getIdentifierType().getDisplayName(),
                mapping.getIdentifierType().getDescription(),
                mapping.getIdentifierType().getDataType().name(),
                mapping.getSortOrder(),
                false, // required - not in mapping
                mapping.isPrimaryCandidate(),
                mapping.getOverrideValidationRegex(),
                mapping.getOverrideDefaultValue(),
                mapping.isActive(),
                mapping.getCreatedAt(),
                mapping.getUpdatedAt(),
                mapping.getEffectiveValidationRegex(),
                mapping.getEffectiveDefaultValue(),
                mapping.getIdentifierType().getValidationRegex(),
                mapping.getIdentifierType().getDefaultValue()
        );
    }
    
    // Constructor
    public IdentityTypeIdentifierMappingResponse(String id, String identityTypeId, String identityTypeName,
                                               String identifierTypeId, String identifierTypeName, 
                                               String identifierTypeDisplayName, String identifierTypeDescription,
                                               String identifierDataType, int sortOrder, boolean required,
                                               boolean primaryCandidate, String overrideValidationRegex,
                                               String overrideDefaultValue, boolean active,
                                               OffsetDateTime createdAt, OffsetDateTime updatedAt,
                                               String effectiveValidationRegex, String effectiveDefaultValue,
                                               String baseValidationRegex, String baseDefaultValue) {
        this.id = id;
        this.identityTypeId = identityTypeId;
        this.identityTypeName = identityTypeName;
        this.identifierTypeId = identifierTypeId;
        this.identifierTypeName = identifierTypeName;
        this.identifierTypeDisplayName = identifierTypeDisplayName;
        this.identifierTypeDescription = identifierTypeDescription;
        this.identifierDataType = identifierDataType;
        this.sortOrder = sortOrder;
        this.required = required;
        this.primaryCandidate = primaryCandidate;
        this.overrideValidationRegex = overrideValidationRegex;
        this.overrideDefaultValue = overrideDefaultValue;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.effectiveValidationRegex = effectiveValidationRegex;
        this.effectiveDefaultValue = effectiveDefaultValue;
        this.baseValidationRegex = baseValidationRegex;
        this.baseDefaultValue = baseDefaultValue;
    }
    
    // Getters
    public String getId() { return id; }
    public String getIdentityTypeId() { return identityTypeId; }
    public String getIdentityTypeName() { return identityTypeName; }
    public String getIdentifierTypeId() { return identifierTypeId; }
    public String getIdentifierTypeName() { return identifierTypeName; }
    public String getIdentifierTypeDisplayName() { return identifierTypeDisplayName; }
    public String getIdentifierTypeDescription() { return identifierTypeDescription; }
    public String getIdentifierDataType() { return identifierDataType; }
    public int getSortOrder() { return sortOrder; }
    public boolean isRequired() { return required; }
    public boolean isPrimaryCandidate() { return primaryCandidate; }
    public String getOverrideValidationRegex() { return overrideValidationRegex; }
    public String getOverrideDefaultValue() { return overrideDefaultValue; }
    public boolean isActive() { return active; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public String getEffectiveValidationRegex() { return effectiveValidationRegex; }
    public String getEffectiveDefaultValue() { return effectiveDefaultValue; }
    public String getBaseValidationRegex() { return baseValidationRegex; }
    public String getBaseDefaultValue() { return baseDefaultValue; }
}
