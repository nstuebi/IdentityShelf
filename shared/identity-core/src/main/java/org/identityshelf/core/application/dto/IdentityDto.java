package org.identityshelf.core.application.dto;

import org.identityshelf.core.domain.valueobject.IdentityId;
import org.identityshelf.core.domain.valueobject.DisplayName;
import org.identityshelf.core.domain.valueobject.IdentityStatus;

import java.time.OffsetDateTime;
import java.util.Map;

/**
 * Data Transfer Object for Identity
 * Used for transferring data between layers
 */
public record IdentityDto(
    IdentityId id,
    DisplayName displayName,
    IdentityStatus status,
    String identityTypeName,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    Map<String, String> attributes
) {
    
    public IdentityDto {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (displayName == null) {
            throw new IllegalArgumentException("Display name cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (identityTypeName == null || identityTypeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Identity type name cannot be null or empty");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at cannot be null");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("Updated at cannot be null");
        }
        if (attributes == null) {
            throw new IllegalArgumentException("Attributes cannot be null");
        }
    }
}
