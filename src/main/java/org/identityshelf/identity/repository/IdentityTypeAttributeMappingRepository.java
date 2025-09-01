package org.identityshelf.identity.repository;

import org.identityshelf.identity.domain.IdentityTypeAttributeMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdentityTypeAttributeMappingRepository extends JpaRepository<IdentityTypeAttributeMapping, String> {
    
    List<IdentityTypeAttributeMapping> findByIdentityTypeIdAndActiveTrue(String identityTypeId);
    
    List<IdentityTypeAttributeMapping> findByAttributeTypeIdAndActiveTrue(String attributeTypeId);
    
    Optional<IdentityTypeAttributeMapping> findByIdentityTypeIdAndAttributeTypeIdAndActiveTrue(
            String identityTypeId, String attributeTypeId);
    
    @Query("SELECT m FROM IdentityTypeAttributeMapping m " +
           "WHERE m.identityType.id = :identityTypeId AND m.active = true " +
           "ORDER BY m.sortOrder ASC")
    List<IdentityTypeAttributeMapping> findActiveByIdentityTypeOrderBySortOrder(@Param("identityTypeId") String identityTypeId);
    
    @Query("SELECT m FROM IdentityTypeAttributeMapping m " +
           "JOIN FETCH m.attributeType " +
           "WHERE m.identityType.id = :identityTypeId AND m.active = true " +
           "ORDER BY m.sortOrder ASC")
    List<IdentityTypeAttributeMapping> findActiveByIdentityTypeWithAttributeType(@Param("identityTypeId") String identityTypeId);
    
    boolean existsByIdentityTypeIdAndAttributeTypeIdAndActiveTrue(String identityTypeId, String attributeTypeId);
}
