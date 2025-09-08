package org.identityshelf.adminapi.web.dto;

import org.identityshelf.core.domain.IdentityIdentifier;

import java.time.OffsetDateTime;

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

    // Constructor
    public IdentityIdentifierResponse(String id, String identityId, String identifierTypeId,
                                    String identifierTypeName, String identifierTypeDisplayName,
                                    String identifierValue, boolean primary, boolean verified,
                                    OffsetDateTime verifiedAt, String verifiedBy, boolean active,
                                    OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.identityId = identityId;
        this.identifierTypeId = identifierTypeId;
        this.identifierTypeName = identifierTypeName;
        this.identifierTypeDisplayName = identifierTypeDisplayName;
        this.identifierValue = identifierValue;
        this.primary = primary;
        this.verified = verified;
        this.verifiedAt = verifiedAt;
        this.verifiedBy = verifiedBy;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
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
    
    // Getters
    public String getId() { return id; }
    public String getIdentityId() { return identityId; }
    public String getIdentifierTypeId() { return identifierTypeId; }
    public String getIdentifierTypeName() { return identifierTypeName; }
    public String getIdentifierTypeDisplayName() { return identifierTypeDisplayName; }
    public String getIdentifierValue() { return identifierValue; }
    public boolean isPrimary() { return primary; }
    public boolean isVerified() { return verified; }
    public OffsetDateTime getVerifiedAt() { return verifiedAt; }
    public String getVerifiedBy() { return verifiedBy; }
    public boolean isActive() { return active; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
