package org.identityshelf.identity.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "identity_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentityType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", columnDefinition = "uuid")
    private UUID uuid;
    
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @Column(name = "display_name", nullable = false)
    private String displayName;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
    
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
    
    @OneToMany(mappedBy = "identityType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IdentityTypeAttributeMapping> attributeMappings = new ArrayList<>();
    
    // JPA lifecycle methods
    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
    
    // Helper methods for attribute mappings
    public void addAttributeMapping(IdentityTypeAttributeMapping mapping) {
        attributeMappings.add(mapping);
        mapping.setIdentityType(this);
    }

    public void removeAttributeMapping(IdentityTypeAttributeMapping mapping) {
        attributeMappings.remove(mapping);
        mapping.setIdentityType(null);
    }
    
    public IdentityTypeAttributeMapping addAttribute(AttributeType attribute, int sortOrder, boolean required) {
        IdentityTypeAttributeMapping mapping = new IdentityTypeAttributeMapping(this, attribute, sortOrder, required);
        addAttributeMapping(mapping);
        return mapping;
    }
}
