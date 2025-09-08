package org.identityshelf.core.domain.valueobject;

import java.util.Objects;

/**
 * Value Object representing an attribute value
 */
public final class AttributeValue {
    private final String value;
    private final String attributeName;
    
    private AttributeValue(String value, String attributeName) {
        if (attributeName == null || attributeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Attribute name cannot be null or empty");
        }
        this.value = value; // Can be null for optional attributes
        this.attributeName = attributeName.trim();
    }
    
    public static AttributeValue of(String value, String attributeName) {
        return new AttributeValue(value, attributeName);
    }
    
    public static AttributeValue empty(String attributeName) {
        return new AttributeValue(null, attributeName);
    }
    
    public String getValue() {
        return value;
    }
    
    public String getAttributeName() {
        return attributeName;
    }
    
    public boolean isEmpty() {
        return value == null || value.trim().isEmpty();
    }
    
    public boolean hasValue() {
        return !isEmpty();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeValue that = (AttributeValue) o;
        return Objects.equals(value, that.value) && 
               Objects.equals(attributeName, that.attributeName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value, attributeName);
    }
    
    @Override
    public String toString() {
        return String.format("%s=%s", attributeName, value);
    }
}
