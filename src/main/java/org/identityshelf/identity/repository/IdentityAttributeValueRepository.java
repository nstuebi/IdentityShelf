package org.identityshelf.identity.repository;

import org.identityshelf.identity.domain.IdentityAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IdentityAttributeValueRepository extends JpaRepository<IdentityAttributeValue, UUID> {
    
    List<IdentityAttributeValue> findByIdentityUuid(UUID identityUuid);
    
    @Query("SELECT iav FROM IdentityAttributeValue iav JOIN FETCH iav.attributeType WHERE iav.identity.uuid = ?1")
    List<IdentityAttributeValue> findByIdentityUuidWithAttributes(UUID identityUuid);
    
    void deleteByIdentityUuid(UUID identityUuid);
    
    @Query("SELECT iav FROM IdentityAttributeValue iav JOIN FETCH iav.attributeType at WHERE iav.identity.uuid = ?1 AND at.name = ?2")
    IdentityAttributeValue findByIdentityUuidAndAttributeName(UUID identityUuid, String attributeName);
}
