package org.identityshelf.data.repository;

import org.identityshelf.core.domain.Identity;
import org.identityshelf.core.domain.valueobject.IdentityId;
import org.identityshelf.core.domain.valueobject.DisplayName;
import org.identityshelf.core.domain.valueobject.IdentityStatus;
import org.identityshelf.core.repository.IdentityRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA implementation of IdentityRepository
 * This implements the domain repository interface using JPA
 */
@Repository
public class JpaIdentityRepository implements IdentityRepository {
    
    private final SpringDataIdentityRepository springDataRepo;
    
    public JpaIdentityRepository(SpringDataIdentityRepository springDataRepo) {
        this.springDataRepo = springDataRepo;
    }
    
    @Override
    public Identity save(Identity identity) {
        // Convert domain entity to JPA entity and save
        org.identityshelf.data.entity.Identity entity = convertToEntity(identity);
        org.identityshelf.data.entity.Identity savedEntity = springDataRepo.save(entity);
        return convertToDomain(savedEntity);
    }
    
    @Override
    public Optional<Identity> findById(IdentityId id) {
        return springDataRepo.findById(id.getValue())
                .map(this::convertToDomain);
    }
    
    @Override
    public Optional<Identity> findByDisplayNameAndType(DisplayName displayName, String identityTypeName) {
        return springDataRepo.findByDisplayNameAndIdentityTypeName(
                displayName.getValue(), identityTypeName)
                .map(this::convertToDomain);
    }
    
    @Override
    public boolean existsByDisplayNameAndType(DisplayName displayName, String identityTypeName) {
        return springDataRepo.existsByDisplayNameAndIdentityTypeName(
                displayName.getValue(), identityTypeName);
    }
    
    @Override
    public boolean existsByDisplayNameAndType(DisplayName displayName, String identityTypeName, IdentityId excludeId) {
        return springDataRepo.existsByDisplayNameAndIdentityTypeNameAndUuidNot(
                displayName.getValue(), identityTypeName, excludeId.getValue());
    }
    
    @Override
    public List<Identity> findByType(String identityTypeName) {
        return springDataRepo.findByIdentityTypeName(identityTypeName)
                .stream()
                .map(this::convertToDomain)
                .toList();
    }
    
    @Override
    public List<Identity> findByStatus(String status) {
        return springDataRepo.findByStatus(status)
                .stream()
                .map(this::convertToDomain)
                .toList();
    }
    
    @Override
    public void deleteById(IdentityId id) {
        springDataRepo.deleteById(id.getValue());
    }
    
    @Override
    public long countByType(String identityTypeName) {
        return springDataRepo.countByIdentityTypeName(identityTypeName);
    }
    
    @Override
    public long countByStatus(String status) {
        return springDataRepo.countByStatus(status);
    }
    
    // Conversion methods between domain and JPA entities
    private org.identityshelf.data.entity.Identity convertToEntity(Identity domain) {
        org.identityshelf.data.entity.Identity entity = new org.identityshelf.data.entity.Identity();
        entity.setUuid(domain.getUuid());
        entity.setDisplayName(domain.getDisplayName().getValue());
        entity.setStatus(domain.getStatus().name());
        entity.setIdentityTypeName(domain.getIdentityTypeName());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
    
    private Identity convertToDomain(org.identityshelf.data.entity.Identity entity) {
        // Create domain identity using reflection to access private constructor
        // This is necessary because the domain Identity only has a factory method
        try {
            java.lang.reflect.Constructor<Identity> constructor = Identity.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Identity identity = constructor.newInstance();
            
            // Set fields using reflection
            java.lang.reflect.Field idField = Identity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(identity, IdentityId.of(entity.getUuid()));
            
            java.lang.reflect.Field displayNameField = Identity.class.getDeclaredField("displayName");
            displayNameField.setAccessible(true);
            displayNameField.set(identity, DisplayName.of(entity.getDisplayName()));
            
            java.lang.reflect.Field statusField = Identity.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(identity, IdentityStatus.fromString(entity.getStatus()));
            
            java.lang.reflect.Field typeField = Identity.class.getDeclaredField("identityTypeName");
            typeField.setAccessible(true);
            typeField.set(identity, entity.getIdentityTypeName());
            
            java.lang.reflect.Field createdAtField = Identity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(identity, entity.getCreatedAt());
            
            java.lang.reflect.Field updatedAtField = Identity.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(identity, entity.getUpdatedAt());
            
            return identity;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JPA entity to domain entity", e);
        }
    }
    
    /**
     * Spring Data JPA repository interface
     * This handles the actual database operations
     */
    public interface SpringDataIdentityRepository extends JpaRepository<org.identityshelf.data.entity.Identity, UUID> {
        Optional<org.identityshelf.data.entity.Identity> findByDisplayNameAndIdentityTypeName(String displayName, String identityTypeName);
        boolean existsByDisplayNameAndIdentityTypeName(String displayName, String identityTypeName);
        boolean existsByDisplayNameAndIdentityTypeNameAndUuidNot(String displayName, String identityTypeName, UUID excludeId);
        List<org.identityshelf.data.entity.Identity> findByIdentityTypeName(String identityTypeName);
        List<org.identityshelf.data.entity.Identity> findByStatus(String status);
        long countByIdentityTypeName(String identityTypeName);
        long countByStatus(String status);
    }
}
