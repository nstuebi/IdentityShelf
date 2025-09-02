package org.identityshelf.identity.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "identifier_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"uuid", "createdAt", "updatedAt"})
public class IdentifierType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", columnDefinition = "uuid")
    private UUID uuid;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "display_name", nullable = false, length = 200)
    private String displayName;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false)
    private AttributeDataType dataType;

    @Column(name = "validation_regex", length = 500)
    private String validationRegex;

    @Column(name = "default_value", length = 500)
    private String defaultValue;

    @Column(name = "is_unique", nullable = false)
    private boolean unique = true; // Identifiers are typically unique by nature

    @Column(name = "is_searchable", nullable = false)
    private boolean searchable = true; // Enable/disable search indexing

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "identifierType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IdentityTypeIdentifierMapping> identityTypeMappings = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    // Constructor for creating new identifier types
    public IdentifierType(String name, String displayName, String description, 
                         AttributeDataType dataType, boolean unique, boolean searchable) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.dataType = dataType;
        this.unique = unique;
        this.searchable = searchable;
        this.active = true;
    }
}
