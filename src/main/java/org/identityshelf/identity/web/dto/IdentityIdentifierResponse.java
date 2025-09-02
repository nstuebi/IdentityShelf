package org.identityshelf.identity.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.identityshelf.identity.domain.IdentityIdentifier;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class IdentityIdentifierResponse {
    private final String id;
    private final String identityId;
    private final String identifierTypeId;
    private final String identifierTypeName;
    private final String identifierTypeDisplayName;
    private final String identifierValue;
    private final boolean primary;
    private final boolean verified;
    private final OffsetDateTime verifiedAt;
    private final String verifiedBy;
    private final boolean active;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    public static IdentityIdentifierResponse from(IdentityIdentifier identifier) {
        return new IdentityIdentifierResponse(
                identifier.getUuid().toString(),
                identifier.getIdentity().getUuid().toString(),
                identifier.getIdentifierType().getUuid().toString(),
                identifier.getIdentifierType().getName(),
                identifier.getIdentifierType().getDisplayName(),
                identifier.getIdentifierValue(),
                identifier.isPrimary(),
                identifier.isVerified(),
                identifier.getVerifiedAt(),
                identifier.getVerifiedBy(),
                identifier.isActive(),
                identifier.getCreatedAt(),
                identifier.getUpdatedAt()
        );
    }
}
