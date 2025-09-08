package org.identityshelf.core.application.command;

import org.identityshelf.core.domain.valueobject.DisplayName;
import org.identityshelf.core.domain.valueobject.AttributeValue;

import java.util.Map;

/**
 * Command to create a new identity
 */
public record CreateIdentityCommand(
    DisplayName displayName,
    String identityTypeName,
    Map<String, AttributeValue> attributes
) {
    
    public CreateIdentityCommand {
        if (displayName == null) {
            throw new IllegalArgumentException("Display name cannot be null");
        }
        if (identityTypeName == null || identityTypeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Identity type name cannot be null or empty");
        }
        if (attributes == null) {
            throw new IllegalArgumentException("Attributes cannot be null");
        }
    }
}
