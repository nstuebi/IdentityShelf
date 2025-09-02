package org.identityshelf.identity.repository;

import org.identityshelf.identity.domain.IdentityTypeIdentifierMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdentityTypeIdentifierMappingRepository extends JpaRepository<IdentityTypeIdentifierMapping, UUID> {
    
    List<IdentityTypeIdentifierMapping> findByActiveTrue();
    
    List<IdentityTypeIdentifierMapping> findByIdentityTypeUuidAndActiveTrue(UUID identityTypeUuid);
    
    List<IdentityTypeIdentifierMapping> findByIdentifierTypeUuidAndActiveTrue(UUID identifierTypeUuid);
    
    Optional<IdentityTypeIdentifierMapping> findByIdentityTypeUuidAndIdentifierTypeUuidAndActiveTrue(
            UUID identityTypeUuid, UUID identifierTypeUuid);
    
    @Query("SELECT m FROM IdentityTypeIdentifierMapping m " +
           "WHERE m.identityType.uuid = :identityTypeUuid AND m.active = true " +
           "ORDER BY m.sortOrder ASC")
    List<IdentityTypeIdentifierMapping> findActiveByIdentityTypeOrderBySortOrder(@Param("identityTypeUuid") UUID identityTypeUuid);
    
    @Query("SELECT m FROM IdentityTypeIdentifierMapping m " +
           "JOIN FETCH m.identifierType " +
           "JOIN FETCH m.identityType " +
           "WHERE m.identityType.uuid = :identityTypeUuid AND m.active = true " +
           "ORDER BY m.sortOrder ASC")
    List<IdentityTypeIdentifierMapping> findActiveByIdentityTypeWithIdentifierType(@Param("identityTypeUuid") UUID identityTypeUuid);
    
    boolean existsByIdentityTypeUuidAndIdentifierTypeUuidAndActiveTrue(UUID identityTypeUuid, UUID identifierTypeUuid);
    
    // Find required identifier mappings for an identity type
    @Query("SELECT m FROM IdentityTypeIdentifierMapping m " +
           "JOIN FETCH m.identifierType " +
           "WHERE m.identityType.uuid = :identityTypeUuid AND m.required = true AND m.active = true " +
           "ORDER BY m.sortOrder ASC")
    List<IdentityTypeIdentifierMapping> findRequiredByIdentityType(@Param("identityTypeUuid") UUID identityTypeUuid);
    
    // Find primary candidate identifier mappings for an identity type
    @Query("SELECT m FROM IdentityTypeIdentifierMapping m " +
           "JOIN FETCH m.identifierType " +
           "WHERE m.identityType.uuid = :identityTypeUuid AND m.primaryCandidate = true AND m.active = true " +
           "ORDER BY m.sortOrder ASC")
    List<IdentityTypeIdentifierMapping> findPrimaryCandidatesByIdentityType(@Param("identityTypeUuid") UUID identityTypeUuid);
}
