package org.identityshelf.identity.repository;

import org.identityshelf.identity.domain.IdentityAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IdentityAttributeValueRepository extends JpaRepository<IdentityAttributeValue, String> {
    
    List<IdentityAttributeValue> findByIdentityId(UUID identityId);
    
    @Query("SELECT iav FROM IdentityAttributeValue iav JOIN FETCH iav.attributeType WHERE iav.identity.id = ?1")
    List<IdentityAttributeValue> findByIdentityIdWithAttributes(UUID identityId);
    
    void deleteByIdentityId(UUID identityId);
    
    @Query("SELECT iav FROM IdentityAttributeValue iav JOIN FETCH iav.attributeType at WHERE iav.identity.id = ?1 AND at.name = ?2")
    IdentityAttributeValue findByIdentityIdAndAttributeName(UUID identityId, String attributeName);
}
