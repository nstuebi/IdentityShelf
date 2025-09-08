package org.identityshelf.adminapi.web.dto;

import org.identityshelf.core.domain.IdentityTypeAttributeMapping;
import java.time.OffsetDateTime;

public class IdentityTypeAttributeMappingResponse {
    private final String id;
    private final String identityTypeId;
    private final String identityTypeName;
    private final String attributeTypeId;
    private final String attributeTypeName;
    private final String attributeTypeDisplayName;
    private final String attributeTypeDescription;
    private final String attributeDataType;
    private final int sortOrder;
    private final boolean required;
    private final String overrideValidationRegex;
    private final String overrideDefaultValue;
    private final boolean active;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
    
    // Effective values (considering overrides)
    private final String effectiveValidationRegex;
    private final String effectiveDefaultValue;
    private final String baseValidationRegex;
    private final String baseDefaultValue;
    
    // Constructor
    public IdentityTypeAttributeMappingResponse(String id, String identityTypeId, String identityTypeName,
                                              String attributeTypeId, String attributeTypeName, 
                                              String attributeTypeDisplayName, String attributeTypeDescription,
                                              String attributeDataType, int sortOrder, boolean required,
                                              String overrideValidationRegex, String overrideDefaultValue,
                                              boolean active, OffsetDateTime createdAt, OffsetDateTime updatedAt,
                                              String effectiveValidationRegex, String effectiveDefaultValue,
                                              String baseValidationRegex, String baseDefaultValue) {
        this.id = id;
        this.identityTypeId = identityTypeId;
        this.identityTypeName = identityTypeName;
        this.attributeTypeId = attributeTypeId;
        this.attributeTypeName = attributeTypeName;
        this.attributeTypeDisplayName = attributeTypeDisplayName;
        this.attributeTypeDescription = attributeTypeDescription;
        this.attributeDataType = attributeDataType;
        this.sortOrder = sortOrder;
        this.required = required;
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
    
    public static IdentityTypeAttributeMappingResponse from(IdentityTypeAttributeMapping mapping) {
        return new IdentityTypeAttributeMappingResponse(
                mapping.getUuid().toString(),
                mapping.getIdentityType().getUuid().toString(),
                mapping.getIdentityType().getName(),
                mapping.getAttributeType().getUuid().toString(),
                mapping.getAttributeType().getName(),
                mapping.getAttributeType().getDisplayName(),
                mapping.getAttributeType().getDescription(),
                mapping.getAttributeType().getDataType().name(),
                mapping.getSortOrder(),
                false, // required - not in mapping
                mapping.getOverrideValidationRegex(),
                mapping.getOverrideDefaultValue(),
                mapping.isActive(),
                mapping.getCreatedAt(),
                mapping.getUpdatedAt(),
                mapping.getEffectiveValidationRegex(),
                mapping.getEffectiveDefaultValue(),
                mapping.getAttributeType().getValidationRegex(),
                mapping.getAttributeType().getDefaultValue()
        );
    }
    
    // Getters
    public String getId() { return id; }
    public String getIdentityTypeId() { return identityTypeId; }
    public String getIdentityTypeName() { return identityTypeName; }
    public String getAttributeTypeId() { return attributeTypeId; }
    public String getAttributeTypeName() { return attributeTypeName; }
    public String getAttributeTypeDisplayName() { return attributeTypeDisplayName; }
    public String getAttributeTypeDescription() { return attributeTypeDescription; }
    public String getAttributeDataType() { return attributeDataType; }
    public int getSortOrder() { return sortOrder; }
    public boolean isRequired() { return required; }
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
