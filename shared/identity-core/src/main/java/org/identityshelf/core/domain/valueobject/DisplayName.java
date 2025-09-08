package org.identityshelf.core.domain.valueobject;

import java.util.Objects;

/**
 * Value Object representing a display name for an Identity
 */
public final class DisplayName {
    private final String value;
    
    private DisplayName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("DisplayName cannot be null or empty");
        }
        if (value.length() > 255) {
            throw new IllegalArgumentException("DisplayName cannot exceed 255 characters");
        }
        this.value = value.trim();
    }
    
    public static DisplayName of(String value) {
        return new DisplayName(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisplayName that = (DisplayName) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
