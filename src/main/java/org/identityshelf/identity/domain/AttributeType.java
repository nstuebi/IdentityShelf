package org.identityshelf.identity.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "attribute_types")
public class AttributeType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_type_id", nullable = false)
    private IdentityType identityType;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "display_name", nullable = false)
    private String displayName;
    
    @Column(name = "description")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false)
    private AttributeDataType dataType;
    
    @Column(name = "is_required", nullable = false)
    private boolean required = false;
    
    @Column(name = "default_value")
    private String defaultValue;
    
    @Column(name = "validation_regex")
    private String validationRegex;
    
    @Column(name = "sort_order", nullable = false)
    private int sortOrder = 0;
    
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
    
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
    
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
    public AttributeType() {}
    
    public AttributeType(String name, String displayName, AttributeDataType dataType) {
        this.name = name;
        this.displayName = displayName;
        this.dataType = dataType;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public IdentityType getIdentityType() {
        return identityType;
    }
    
    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
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
    
    public AttributeDataType getDataType() {
        return dataType;
    }
    
    public void setDataType(AttributeDataType dataType) {
        this.dataType = dataType;
    }
    
    public boolean isRequired() {
        return required;
    }
    
    public void setRequired(boolean required) {
        this.required = required;
    }
    
    public String getDefaultValue() {
        return defaultValue;
    }
    
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public String getValidationRegex() {
        return validationRegex;
    }
    
    public void setValidationRegex(String validationRegex) {
        this.validationRegex = validationRegex;
    }
    
    public int getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
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
}
