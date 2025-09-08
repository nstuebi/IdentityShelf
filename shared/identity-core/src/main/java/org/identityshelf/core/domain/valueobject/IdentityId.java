package org.identityshelf.core.domain.valueobject;

import java.util.UUID;
import java.util.Objects;

/**
 * Value Object representing an Identity's unique identifier
 */
public final class IdentityId {
    private final UUID value;
    
    private IdentityId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("IdentityId cannot be null");
        }
        this.value = value;
    }
    
    public static IdentityId of(UUID value) {
        return new IdentityId(value);
    }
    
    public static IdentityId generate() {
        return new IdentityId(UUID.randomUUID());
    }
    
    public UUID getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentityId that = (IdentityId) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}
