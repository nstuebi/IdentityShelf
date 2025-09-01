package org.identityshelf.identity.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "identities")
@Data
@NoArgsConstructor
public class Identity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 320)
    private String email;

    @Column(name = "display_name", nullable = false, length = 255)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private IdentityStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_type_id", nullable = false)
    private IdentityType identityType;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "identity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IdentityAttributeValue> values = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        if (this.status == null) {
            this.status = IdentityStatus.ACTIVE;
        }
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    // Helper methods for managing values
    public void addValue(IdentityAttributeValue value) {
        values.add(value);
        value.setIdentity(this);
    }

    public void removeValue(IdentityAttributeValue value) {
        values.remove(value);
        value.setIdentity(null);
    }

    public IdentityAttributeValue getValueForAttribute(String attributeName) {
        return values.stream()
            .filter(v -> v.getAttributeType() != null && 
                        attributeName.equals(v.getAttributeType().getName()))
            .findFirst()
            .orElse(null);
    }
}


