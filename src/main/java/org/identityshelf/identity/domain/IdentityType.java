package org.identityshelf.identity.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "identity_types")
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
    
    // Constructors
    public IdentityType() {}
    
    public IdentityType(String name, String displayName, String description) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
        public List<AttributeType> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeType> attributes) {
        this.attributes = attributes;
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
