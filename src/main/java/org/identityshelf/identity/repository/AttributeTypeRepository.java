package org.identityshelf.identity.repository;

import org.identityshelf.identity.domain.AttributeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AttributeTypeRepository extends JpaRepository<AttributeType, UUID> {
    
    List<AttributeType> findByActiveTrueOrderByName();
}
