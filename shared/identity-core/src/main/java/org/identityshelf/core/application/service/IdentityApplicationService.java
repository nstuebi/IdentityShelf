package org.identityshelf.core.application.service;

import org.identityshelf.core.domain.Identity;
import org.identityshelf.core.domain.valueobject.IdentityId;
import org.identityshelf.core.domain.valueobject.DisplayName;
import org.identityshelf.core.domain.valueobject.AttributeValue;
import org.identityshelf.core.repository.IdentityRepository;
import org.identityshelf.core.domain.service.IdentityDomainService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Application Service for Identity use cases
 * Orchestrates domain objects and repositories to fulfill use cases
 */
public class IdentityApplicationService {
    
    private final IdentityRepository identityRepository;
    private final IdentityDomainService identityDomainService;
    
    public IdentityApplicationService(IdentityRepository identityRepository, 
                                   IdentityDomainService identityDomainService) {
        this.identityRepository = identityRepository;
        this.identityDomainService = identityDomainService;
    }
    
    /**
     * Create a new identity
     */
    public Identity createIdentity(DisplayName displayName, String identityTypeName, 
                                 Map<String, AttributeValue> attributes) {
        // Validate business rules
        if (!identityDomainService.canCreateIdentity(identityTypeName, attributes)) {
            throw new IllegalArgumentException("Cannot create identity with given attributes");
        }
        
        // Generate unique display name if needed
        DisplayName uniqueDisplayName = identityDomainService.generateUniqueDisplayName(displayName, identityTypeName);
        
        // Create the aggregate
        Identity identity = Identity.create(uniqueDisplayName, identityTypeName, attributes);
        
        // Save and return
        return identityRepository.save(identity);
    }
    
    /**
     * Update an existing identity
     */
    public Identity updateIdentity(IdentityId id, DisplayName newDisplayName, 
                                 Map<String, AttributeValue> attributes) {
        Identity identity = identityRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Identity not found: " + id));
        
        // Validate business rules
        if (!identityDomainService.canUpdateIdentity(id, attributes)) {
            throw new IllegalArgumentException("Cannot update identity with given attributes");
        }
        
        // Check if display name is unique (if changed)
        if (!identity.getDisplayName().equals(newDisplayName)) {
            if (identityDomainService.displayNameExists(newDisplayName, identity.getIdentityTypeName(), id)) {
                throw new IllegalArgumentException("Display name already exists: " + newDisplayName);
            }
        }
        
        // Update the aggregate
        identity.updateDisplayName(newDisplayName);
        
        // Save and return
        return identityRepository.save(identity);
    }
    
    /**
     * Activate an identity
     */
    public Identity activateIdentity(IdentityId id) {
        Identity identity = identityRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Identity not found: " + id));
        
        identity.activate();
        return identityRepository.save(identity);
    }
    
    /**
     * Deactivate an identity
     */
    public Identity deactivateIdentity(IdentityId id) {
        Identity identity = identityRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Identity not found: " + id));
        
        identity.deactivate();
        return identityRepository.save(identity);
    }
    
    /**
     * Suspend an identity
     */
    public Identity suspendIdentity(IdentityId id) {
        Identity identity = identityRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Identity not found: " + id));
        
        identity.suspend();
        return identityRepository.save(identity);
    }
    
    /**
     * Find identity by ID
     */
    public Optional<Identity> findById(IdentityId id) {
        return identityRepository.findById(id);
    }
    
    /**
     * Find identity by display name and type
     */
    public Optional<Identity> findByDisplayNameAndType(DisplayName displayName, String identityTypeName) {
        return identityRepository.findByDisplayNameAndType(displayName, identityTypeName);
    }
    
    /**
     * Find all identities by type
     */
    public List<Identity> findByType(String identityTypeName) {
        return identityRepository.findByType(identityTypeName);
    }
    
    /**
     * Find all identities by status
     */
    public List<Identity> findByStatus(String status) {
        return identityRepository.findByStatus(status);
    }
    
    /**
     * Delete identity
     */
    public void deleteIdentity(IdentityId id) {
        Identity identity = identityRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Identity not found: " + id));
        
        identityRepository.deleteById(id);
    }
}
