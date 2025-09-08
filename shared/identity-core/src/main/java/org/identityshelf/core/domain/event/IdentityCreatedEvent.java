package org.identityshelf.core.domain.event;

import org.identityshelf.core.domain.valueobject.IdentityId;
import org.identityshelf.core.domain.valueobject.DisplayName;
import org.identityshelf.core.domain.valueobject.IdentityStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Domain event fired when an Identity is created
 */
public record IdentityCreatedEvent(
    UUID eventId,
    OffsetDateTime occurredOn,
    IdentityId identityId,
    DisplayName displayName,
    IdentityStatus status,
    String identityTypeName
) implements DomainEvent {
    
    public IdentityCreatedEvent(IdentityId identityId, DisplayName displayName, 
                              IdentityStatus status, String identityTypeName) {
        this(UUID.randomUUID(), OffsetDateTime.now(), identityId, displayName, status, identityTypeName);
    }
    
    @Override
    public UUID getEventId() {
        return eventId;
    }
    
    @Override
    public OffsetDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public String getEventType() {
        return "IdentityCreated";
    }
}
