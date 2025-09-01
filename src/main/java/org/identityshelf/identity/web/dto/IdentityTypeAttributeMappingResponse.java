package org.identityshelf.identity.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.identityshelf.identity.domain.IdentityTypeAttributeMapping;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
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
                mapping.isRequired(),
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
}
