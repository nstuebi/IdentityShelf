package org.identityshelf.core.domain.service;

import org.identityshelf.core.domain.Identity;
import org.identityshelf.core.domain.valueobject.IdentityId;
import org.identityshelf.core.domain.valueobject.DisplayName;
import org.identityshelf.core.domain.valueobject.AttributeValue;

import java.util.Map;
import java.util.Optional;

/**
 * Domain Service for Identity business logic that doesn't belong to a single aggregate
 */
public interface IdentityDomainService {
    
    /**
     * Validates if an identity can be created with the given attributes
     */
    boolean canCreateIdentity(String identityTypeName, Map<String, AttributeValue> attributes);
    
    /**
     * Validates if an identity can be updated with the given attributes
     */
    boolean canUpdateIdentity(IdentityId identityId, Map<String, AttributeValue> attributes);
    
    /**
     * Generates a unique display name if the provided one is not available
     */
    DisplayName generateUniqueDisplayName(DisplayName requestedName, String identityTypeName);
    
    /**
     * Validates attribute values against identity type constraints
     */
    boolean validateAttributeValues(String identityTypeName, Map<String, AttributeValue> attributes);
    
    /**
     * Checks if an identity with the given display name already exists
     */
    boolean displayNameExists(DisplayName displayName, String identityTypeName);
    
    /**
     * Checks if an identity with the given display name already exists (excluding current identity)
     */
    boolean displayNameExists(DisplayName displayName, String identityTypeName, IdentityId excludeIdentityId);
}
