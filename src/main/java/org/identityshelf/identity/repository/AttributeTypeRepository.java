package org.identityshelf.identity.repository;

import org.identityshelf.identity.domain.AttributeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeTypeRepository extends JpaRepository<AttributeType, String> {
    
    List<AttributeType> findByIdentityTypeIdAndActiveTrueOrderBySortOrder(String identityTypeId);
    
    @Query("SELECT at FROM AttributeType at WHERE at.identityType.id = ?1 AND at.active = true ORDER BY at.sortOrder")
    List<AttributeType> findActiveAttributesByTypeId(String identityTypeId);
    
    List<AttributeType> findByIdentityTypeNameAndActiveTrueOrderBySortOrder(String typeName);
}
