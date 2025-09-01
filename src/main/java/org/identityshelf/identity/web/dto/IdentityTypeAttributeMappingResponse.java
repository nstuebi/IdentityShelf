package org.identityshelf.identity.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
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
}
