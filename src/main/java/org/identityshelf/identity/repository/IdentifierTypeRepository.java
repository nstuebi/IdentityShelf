package org.identityshelf.identity.repository;

import org.identityshelf.identity.domain.IdentifierType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdentifierTypeRepository extends JpaRepository<IdentifierType, UUID> {

    List<IdentifierType> findByActiveTrueOrderByDisplayName();
    
    List<IdentifierType> findBySearchableTrueAndActiveTrueOrderByDisplayName();
    
    Optional<IdentifierType> findByNameAndActiveTrue(String name);
    
    boolean existsByName(String name);
    
    @Query("SELECT it FROM IdentifierType it WHERE it.active = true AND it.searchable = true ORDER BY it.displayName")
    List<IdentifierType> findActiveSearchableTypes();
}
