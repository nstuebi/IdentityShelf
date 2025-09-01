package org.identityshelf.identity.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "identity_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentityType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
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
    private List<AttributeType> attributes = new ArrayList<>();
    
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
    
    // Helper methods
    public void addAttribute(AttributeType attribute) {
        attributes.add(attribute);
        attribute.setIdentityType(this);
    }

    public void removeAttribute(AttributeType attribute) {
        attributes.remove(attribute);
        attribute.setIdentityType(null);
    }
}
