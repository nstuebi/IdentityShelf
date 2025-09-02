package org.identityshelf.identity.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.identityshelf.identity.domain.IdentifierType;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
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
}
