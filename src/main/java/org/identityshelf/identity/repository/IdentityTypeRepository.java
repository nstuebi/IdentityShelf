package org.identityshelf.identity.repository;

import org.identityshelf.identity.domain.IdentityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdentityTypeRepository extends JpaRepository<IdentityType, String> {
    
    Optional<IdentityType> findByName(String name);
    
    List<IdentityType> findByActiveTrue();
    
    @Query("SELECT it FROM IdentityType it LEFT JOIN FETCH it.attributes WHERE it.active = true ORDER BY it.displayName")
    List<IdentityType> findActiveTypesWithAttributes();
    
    boolean existsByName(String name);
}
