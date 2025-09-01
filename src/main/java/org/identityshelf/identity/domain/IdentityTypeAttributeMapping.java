package org.identityshelf.identity.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "identity_type_attribute_mappings", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"identity_type_id", "attribute_type_id"}))
@Data
@NoArgsConstructor
public class IdentityTypeAttributeMapping {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_type_id", nullable = false)
    private IdentityType identityType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_type_id", nullable = false)
    private AttributeType attributeType;
    
    // Mapping-specific fields
    @Column(name = "sort_order", nullable = false)
    private int sortOrder = 0;
    
    @Column(name = "is_required", nullable = false)
    private boolean required = false;
    
    // Override fields (null means use default from AttributeType)
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
    
    // Convenience constructor
    public IdentityTypeAttributeMapping(IdentityType identityType, AttributeType attributeType, 
                                       int sortOrder, boolean required) {
        this.identityType = identityType;
        this.attributeType = attributeType;
        this.sortOrder = sortOrder;
        this.required = required;
    }
    
    // Helper methods for effective values (with overrides)
    public String getEffectiveValidationRegex() {
        return overrideValidationRegex != null ? overrideValidationRegex : 
               (attributeType != null ? attributeType.getValidationRegex() : null);
    }
    
    public String getEffectiveDefaultValue() {
        return overrideDefaultValue != null ? overrideDefaultValue : 
               (attributeType != null ? attributeType.getDefaultValue() : null);
    }
    
    /**
     * Validates value against cumulative rules (both base and override if present)
     */
    public boolean validateValue(String value) {
        if (value == null || value.isEmpty()) {
            return !required;
        }
        
        // Validate against base attribute regex if present
        if (attributeType != null && attributeType.getValidationRegex() != null) {
            if (!value.matches(attributeType.getValidationRegex())) {
                return false;
            }
        }
        
        // Additionally validate against override regex if present
        if (overrideValidationRegex != null) {
            if (!value.matches(overrideValidationRegex)) {
                return false;
            }
        }
        
        return true;
    }
}
