package org.identityshelf.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "identities")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"uuid", "createdAt", "updatedAt"})
public class Identity {

    @Id
    @Column(name = "uuid", nullable = false, columnDefinition = "uuid")
    private UUID uuid;

    @Column(name = "display_name", nullable = false, length = 255)
    private String displayName;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "identity_type_name", nullable = false)
    private String identityTypeName;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public Identity(String displayName, String status, String identityTypeName) {
        this.displayName = displayName;
        this.status = status;
        this.identityTypeName = identityTypeName;
    }

    @PrePersist
    public void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
        if (this.status == null) {
            this.status = "ACTIVE";
        }
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
