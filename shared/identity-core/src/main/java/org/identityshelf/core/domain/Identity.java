package org.identityshelf.core.domain;

import org.identityshelf.core.domain.valueobject.IdentityId;
import org.identityshelf.core.domain.valueobject.DisplayName;
import org.identityshelf.core.domain.valueobject.IdentityStatus;
import org.identityshelf.core.domain.valueobject.AttributeValue;
import org.identityshelf.core.domain.event.DomainEvent;
import org.identityshelf.core.domain.event.IdentityCreatedEvent;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Objects;

/**
 * Aggregate Root for Identity
 * Encapsulates all business logic and invariants for Identity management
 */
public class Identity {
    private IdentityId id;
    private DisplayName displayName;
    private IdentityStatus status;
    private String identityTypeName;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
    // Domain events
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    
    // Private constructor for aggregate creation
    private Identity() {}
    
    /**
     * Factory method to create a new Identity
     */
    public static Identity create(DisplayName displayName, String identityTypeName, 
                                Map<String, AttributeValue> attributes) {
        Identity identity = new Identity();
        identity.id = IdentityId.generate();
        identity.displayName = displayName;
        identity.status = IdentityStatus.ACTIVE;
        identity.identityTypeName = identityTypeName;
        identity.createdAt = OffsetDateTime.now();
        identity.updatedAt = identity.createdAt;
        
        // Add domain event
        identity.addDomainEvent(new IdentityCreatedEvent(
            identity.id, identity.displayName, identity.status, identityTypeName));
        
        return identity;
    }
    
    /**
     * Business method to activate the identity
     */
    public void activate() {
        if (this.status == IdentityStatus.ACTIVE) {
            throw new IllegalStateException("Identity is already active");
        }
        this.status = IdentityStatus.ACTIVE;
        this.updatedAt = OffsetDateTime.now();
    }
    
    /**
     * Business method to deactivate the identity
     */
    public void deactivate() {
        if (this.status == IdentityStatus.INACTIVE) {
            throw new IllegalStateException("Identity is already inactive");
        }
        this.status = IdentityStatus.INACTIVE;
        this.updatedAt = OffsetDateTime.now();
    }
    
    /**
     * Business method to suspend the identity
     */
    public void suspend() {
        if (this.status == IdentityStatus.SUSPENDED) {
            throw new IllegalStateException("Identity is already suspended");
        }
        this.status = IdentityStatus.SUSPENDED;
        this.updatedAt = OffsetDateTime.now();
    }
    
    /**
     * Business method to update display name
     */
    public void updateDisplayName(DisplayName newDisplayName) {
        if (!this.status.canBeModified()) {
            throw new IllegalStateException("Cannot modify identity with status: " + this.status);
        }
        this.displayName = newDisplayName;
        this.updatedAt = OffsetDateTime.now();
    }
    
    /**
     * Business method to check if identity can be modified
     */
    public boolean canBeModified() {
        return this.status.canBeModified();
    }
    
    /**
     * Business method to check if identity is active
     */
    public boolean isActive() {
        return this.status.isActive();
    }
    
    // Getters
    public IdentityId getId() {
        return id;
    }
    
    public java.util.UUID getUuid() {
        return id.getValue();
    }
    
    public DisplayName getDisplayName() {
        return displayName;
    }
    
    public IdentityStatus getStatus() {
        return status;
    }
    
    public String getIdentityTypeName() {
        return identityTypeName;
    }
    
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    // Domain events management
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
    
    public void clearDomainEvents() {
        domainEvents.clear();
    }
    
    private void addDomainEvent(DomainEvent event) {
        domainEvents.add(event);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identity identity = (Identity) o;
        return Objects.equals(id, identity.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Identity{id=%s, displayName=%s, status=%s, type=%s}", 
                           id, displayName, status, identityTypeName);
    }
}
