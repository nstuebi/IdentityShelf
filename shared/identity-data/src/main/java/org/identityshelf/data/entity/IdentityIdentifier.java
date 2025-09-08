package org.identityshelf.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "identity_identifiers", 
       indexes = {
           @Index(name = "idx_identity_identifier_value", columnList = "identifier_value"),
           @Index(name = "idx_identity_identifier_type", columnList = "identifier_type_uuid"),
           @Index(name = "idx_identity_identifier_identity", columnList = "identity_uuid"),
           @Index(name = "idx_identity_identifier_search", columnList = "identifier_type_uuid, identifier_value"),
           @Index(name = "idx_identity_identifier_unique", columnList = "identifier_type_uuid, identifier_value", unique = true)
       })
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"uuid", "createdAt", "updatedAt"})
public class IdentityIdentifier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", columnDefinition = "uuid")
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_uuid", nullable = false)
    private Identity identity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identifier_type_uuid", nullable = false)
    private IdentifierType identifierType;

    @Column(name = "identifier_value", nullable = false, length = 500)
    private String identifierValue;

    @Column(name = "is_primary", nullable = false)
    private boolean primary = false; // Mark if this is the primary identifier for the identity

    @Column(name = "is_verified", nullable = false)
    private boolean verified = false; // Track verification status

    @Column(name = "verified_at")
    private OffsetDateTime verifiedAt;

    @Column(name = "verified_by", length = 100)
    private String verifiedBy;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    // Constructor for creating new identifiers
    public IdentityIdentifier(Identity identity, IdentifierType identifierType, 
                             String identifierValue, boolean primary) {
        this.identity = identity;
        this.identifierType = identifierType;
        this.identifierValue = identifierValue;
        this.primary = primary;
        this.active = true;
    }

    // Helper method to mark as verified
    public void markAsVerified(String verifiedBy) {
        this.verified = true;
        this.verifiedAt = OffsetDateTime.now();
        this.verifiedBy = verifiedBy;
    }
}
