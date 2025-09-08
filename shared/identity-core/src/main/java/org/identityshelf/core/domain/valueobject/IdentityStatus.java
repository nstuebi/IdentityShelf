package org.identityshelf.core.domain.valueobject;

import java.util.Objects;

/**
 * Value Object representing the status of an Identity
 */
public enum IdentityStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    PENDING("Pending");
    
    private final String displayName;
    
    IdentityStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    public boolean canBeModified() {
        return this == ACTIVE || this == PENDING;
    }
    
    public static IdentityStatus fromString(String status) {
        if (status == null) {
            return ACTIVE; // Default status
        }
        
        for (IdentityStatus s : values()) {
            if (s.name().equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid identity status: " + status);
    }
}
