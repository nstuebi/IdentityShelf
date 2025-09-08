package org.identityshelf.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "identity_type_identifier_mappings",
       uniqueConstraints = @UniqueConstraint(columnNames = {"identity_type_uuid", "identifier_type_uuid"}))
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"uuid", "createdAt", "updatedAt"})
public class IdentityTypeIdentifierMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", columnDefinition = "uuid")
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_type_uuid", nullable = false)
    private IdentityType identityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identifier_type_uuid", nullable = false)
    private IdentifierType identifierType;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "is_required", nullable = false)
    private boolean required;

    @Column(name = "is_primary_candidate", nullable = false)
    private boolean primaryCandidate = false; // Can this identifier type be used as primary?

    @Column(name = "override_validation_regex")
    private String overrideValidationRegex;

    @Column(name = "override_default_value")
    private String overrideDefaultValue;

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

    // Convenience constructor for adding mappings
    public IdentityTypeIdentifierMapping(IdentityType identityType, IdentifierType identifierType, 
                                        int sortOrder, boolean required, boolean primaryCandidate) {
        this.identityType = identityType;
        this.identifierType = identifierType;
        this.sortOrder = sortOrder;
        this.required = required;
        this.primaryCandidate = primaryCandidate;
        this.active = true;
    }

    // Helper to get effective validation regex (cumulative)
    public String getEffectiveValidationRegex() {
        String baseRegex = identifierType.getValidationRegex();
        if (overrideValidationRegex != null && !overrideValidationRegex.trim().isEmpty()) {
            if (baseRegex != null && !baseRegex.trim().isEmpty()) {
                // Cumulative: both must match, so combine with AND
                return "(?=" + baseRegex + ")" + overrideValidationRegex;
            }
            return overrideValidationRegex;
        }
        return baseRegex;
    }

    // Helper to get effective default value
    public String getEffectiveDefaultValue() {
        return overrideDefaultValue != null && !overrideDefaultValue.trim().isEmpty() ?
               overrideDefaultValue : identifierType.getDefaultValue();
    }
}
