package org.identityshelf.core.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class IdentityIdentifier {
    private UUID uuid;
    private Identity identity;
    private IdentifierType identifierType;
    private String identifierValue;
    private boolean primary;
    private boolean verified;
    private OffsetDateTime verifiedAt;
    private String verifiedBy;
    private boolean active;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
    // Constructors
    public IdentityIdentifier() {}
    
    public IdentityIdentifier(UUID uuid, Identity identity, IdentifierType identifierType, 
                            String identifierValue, boolean primary, boolean verified, 
                            OffsetDateTime verifiedAt, String verifiedBy, boolean active, 
                            OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.uuid = uuid;
        this.identity = identity;
        this.identifierType = identifierType;
        this.identifierValue = identifierValue;
        this.primary = primary;
        this.verified = verified;
        this.verifiedAt = verifiedAt;
        this.verifiedBy = verifiedBy;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and setters
    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }
    public Identity getIdentity() { return identity; }
    public void setIdentity(Identity identity) { this.identity = identity; }
    public IdentifierType getIdentifierType() { return identifierType; }
    public void setIdentifierType(IdentifierType identifierType) { this.identifierType = identifierType; }
    public String getIdentifierValue() { return identifierValue; }
    public void setIdentifierValue(String identifierValue) { this.identifierValue = identifierValue; }
    public boolean isPrimary() { return primary; }
    public void setPrimary(boolean primary) { this.primary = primary; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public OffsetDateTime getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(OffsetDateTime verifiedAt) { this.verifiedAt = verifiedAt; }
    public String getVerifiedBy() { return verifiedBy; }
    public void setVerifiedBy(String verifiedBy) { this.verifiedBy = verifiedBy; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
