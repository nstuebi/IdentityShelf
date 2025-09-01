package org.identityshelf.identity.repository;

import org.identityshelf.identity.domain.IdentityTypeAttributeMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdentityTypeAttributeMappingRepository extends JpaRepository<IdentityTypeAttributeMapping, UUID> {
    
    List<IdentityTypeAttributeMapping> findByIdentityTypeUuidAndActiveTrue(UUID identityTypeUuid);
    
    List<IdentityTypeAttributeMapping> findByAttributeTypeUuidAndActiveTrue(UUID attributeTypeUuid);
    
    Optional<IdentityTypeAttributeMapping> findByIdentityTypeUuidAndAttributeTypeUuidAndActiveTrue(
            UUID identityTypeUuid, UUID attributeTypeUuid);
    
    @Query("SELECT m FROM IdentityTypeAttributeMapping m " +
           "WHERE m.identityType.uuid = :identityTypeUuid AND m.active = true " +
           "ORDER BY m.sortOrder ASC")
    List<IdentityTypeAttributeMapping> findActiveByIdentityTypeOrderBySortOrder(@Param("identityTypeUuid") UUID identityTypeUuid);
    
    @Query("SELECT m FROM IdentityTypeAttributeMapping m " +
           "JOIN FETCH m.attributeType " +
           "JOIN FETCH m.identityType " +
           "WHERE m.identityType.uuid = :identityTypeUuid AND m.active = true " +
           "ORDER BY m.sortOrder ASC")
    List<IdentityTypeAttributeMapping> findActiveByIdentityTypeWithAttributeType(@Param("identityTypeUuid") UUID identityTypeUuid);
    
    boolean existsByIdentityTypeUuidAndAttributeTypeUuidAndActiveTrue(UUID identityTypeUuid, UUID attributeTypeUuid);
}
