package org.identityshelf.identity.repository;

import org.identityshelf.identity.domain.IdentityValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IdentityValueRepository extends JpaRepository<IdentityValue, String> {
    
    List<IdentityValue> findByIdentityId(UUID identityId);
    
    @Query("SELECT iv FROM IdentityValue iv JOIN FETCH iv.identityAttribute WHERE iv.identity.id = ?1")
    List<IdentityValue> findByIdentityIdWithAttributes(UUID identityId);
    
    void deleteByIdentityId(UUID identityId);
    
    @Query("SELECT iv FROM IdentityValue iv JOIN FETCH iv.identityAttribute ia WHERE iv.identity.id = ?1 AND ia.name = ?2")
    IdentityValue findByIdentityIdAndAttributeName(UUID identityId, String attributeName);
}
