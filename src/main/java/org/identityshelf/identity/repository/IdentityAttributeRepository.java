package org.identityshelf.identity.repository;

import org.identityshelf.identity.domain.IdentityAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdentityAttributeRepository extends JpaRepository<IdentityAttribute, String> {
    
    List<IdentityAttribute> findByIdentityTypeIdAndActiveTrueOrderBySortOrder(String identityTypeId);
    
    @Query("SELECT ia FROM IdentityAttribute ia WHERE ia.identityType.id = ?1 AND ia.active = true ORDER BY ia.sortOrder")
    List<IdentityAttribute> findActiveAttributesByTypeId(String identityTypeId);
    
    List<IdentityAttribute> findByIdentityTypeNameAndActiveTrueOrderBySortOrder(String typeName);
}
