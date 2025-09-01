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
    
    List<IdentityTypeAttributeMapping> findByIdentityTypeIdAndActiveTrue(UUID identityTypeId);
    
    List<IdentityTypeAttributeMapping> findByAttributeTypeIdAndActiveTrue(UUID attributeTypeId);
    
    Optional<IdentityTypeAttributeMapping> findByIdentityTypeIdAndAttributeTypeIdAndActiveTrue(
            UUID identityTypeId, UUID attributeTypeId);
    
    @Query("SELECT m FROM IdentityTypeAttributeMapping m " +
           "WHERE m.identityType.id = :identityTypeId AND m.active = true " +
           "ORDER BY m.sortOrder ASC")
    List<IdentityTypeAttributeMapping> findActiveByIdentityTypeOrderBySortOrder(@Param("identityTypeId") UUID identityTypeId);
    
    @Query("SELECT m FROM IdentityTypeAttributeMapping m " +
           "JOIN FETCH m.attributeType " +
           "JOIN FETCH m.identityType " +
           "WHERE m.identityType.id = :identityTypeId AND m.active = true " +
           "ORDER BY m.sortOrder ASC")
    List<IdentityTypeAttributeMapping> findActiveByIdentityTypeWithAttributeType(@Param("identityTypeId") UUID identityTypeId);
    
    boolean existsByIdentityTypeIdAndAttributeTypeIdAndActiveTrue(UUID identityTypeId, UUID attributeTypeId);
}
