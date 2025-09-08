package org.identityshelf.adminapi.service;

import org.identityshelf.core.domain.IdentityType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IdentityTypeService {
    
    public List<IdentityType> getAllIdentityTypes() {
        // TODO: Implement actual service logic
        return List.of();
    }
    
    public IdentityType getIdentityType(UUID id) {
        // TODO: Implement actual service logic
        return null;
    }
    
    public IdentityType createIdentityType(Object request) {
        // TODO: Implement actual service logic
        return null;
    }
    
    public IdentityType updateIdentityType(UUID id, Object request) {
        // TODO: Implement actual service logic
        return null;
    }
    
    public void deleteIdentityType(UUID id) {
        // TODO: Implement actual service logic
    }
    
    public List<IdentityType> getAllActiveTypes() {
        // TODO: Implement
        return List.of();
    }
    
    public IdentityType getTypeByName(String typeName) {
        // TODO: Implement
        return null;
    }
    
    public List<Object> getAttributesForType(String typeName) {
        // TODO: Implement
        return List.of();
    }
}
