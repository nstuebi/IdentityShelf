package org.identityshelf.core.repository;

import org.identityshelf.core.domain.Identity;
import org.identityshelf.core.domain.valueobject.IdentityId;
import org.identityshelf.core.domain.valueobject.DisplayName;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Identity aggregate
 * This is part of the domain layer and defines the contract for persistence
 */
public interface IdentityRepository {
    
    /**
     * Save an identity aggregate
     */
    Identity save(Identity identity);
    
    /**
     * Find identity by ID
     */
    Optional<Identity> findById(IdentityId id);
    
    /**
     * Find identity by display name and type
     */
    Optional<Identity> findByDisplayNameAndType(DisplayName displayName, String identityTypeName);
    
    /**
     * Check if identity exists by display name and type
     */
    boolean existsByDisplayNameAndType(DisplayName displayName, String identityTypeName);
    
    /**
     * Check if identity exists by display name and type (excluding given ID)
     */
    boolean existsByDisplayNameAndType(DisplayName displayName, String identityTypeName, IdentityId excludeId);
    
    /**
     * Find all identities by type
     */
    List<Identity> findByType(String identityTypeName);
    
    /**
     * Find all identities by status
     */
    List<Identity> findByStatus(String status);
    
    /**
     * Delete identity by ID
     */
    void deleteById(IdentityId id);
    
    /**
     * Count identities by type
     */
    long countByType(String identityTypeName);
    
    /**
     * Count identities by status
     */
    long countByStatus(String status);
}
