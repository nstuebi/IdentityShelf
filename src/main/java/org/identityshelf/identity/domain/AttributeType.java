package org.identityshelf.identity.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "attribute_types")
@Data
@NoArgsConstructor
public class AttributeType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", columnDefinition = "uuid")
    private UUID uuid;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "display_name", nullable = false)
    private String displayName;
    
    @Column(name = "description")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false)
    private AttributeDataType dataType;
    
    @Column(name = "default_value")
    private String defaultValue;
    
    @Column(name = "validation_regex")
    private String validationRegex;
    
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
    
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
    
    @OneToMany(mappedBy = "attributeType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IdentityTypeAttributeMapping> identityTypeMappings = new ArrayList<>();
    
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
    
    // Convenience constructor
    public AttributeType(String name, String displayName, AttributeDataType dataType) {
        this.name = name;
        this.displayName = displayName;
        this.dataType = dataType;
    }
    
}
