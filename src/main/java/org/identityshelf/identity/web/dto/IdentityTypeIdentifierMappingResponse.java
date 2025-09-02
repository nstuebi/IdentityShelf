package org.identityshelf.identity.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.identityshelf.identity.domain.IdentityTypeIdentifierMapping;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
        return IdentityTypeIdentifierMappingResponse.builder()
                .id(mapping.getUuid().toString())
                .identityTypeId(mapping.getIdentityType().getUuid().toString())
                .identityTypeName(mapping.getIdentityType().getName())
                .identifierTypeId(mapping.getIdentifierType().getUuid().toString())
                .identifierTypeName(mapping.getIdentifierType().getName())
                .identifierTypeDisplayName(mapping.getIdentifierType().getDisplayName())
                .identifierTypeDescription(mapping.getIdentifierType().getDescription())
                .identifierDataType(mapping.getIdentifierType().getDataType().name())
                .sortOrder(mapping.getSortOrder())
                .required(mapping.isRequired())
                .primaryCandidate(mapping.isPrimaryCandidate())
                .overrideValidationRegex(mapping.getOverrideValidationRegex())
                .overrideDefaultValue(mapping.getOverrideDefaultValue())
                .active(mapping.isActive())
                .createdAt(mapping.getCreatedAt())
                .updatedAt(mapping.getUpdatedAt())
                .effectiveValidationRegex(mapping.getEffectiveValidationRegex())
                .effectiveDefaultValue(mapping.getEffectiveDefaultValue())
                .baseValidationRegex(mapping.getIdentifierType().getValidationRegex())
                .baseDefaultValue(mapping.getIdentifierType().getDefaultValue())
                .build();
    }
}
