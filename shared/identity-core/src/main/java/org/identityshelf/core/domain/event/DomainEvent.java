package org.identityshelf.core.domain.event;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Base interface for all domain events
 */
public interface DomainEvent {
    UUID getEventId();
    OffsetDateTime getOccurredOn();
    String getEventType();
}
