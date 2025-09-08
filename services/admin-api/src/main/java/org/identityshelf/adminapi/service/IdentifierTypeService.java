package org.identityshelf.adminapi.service;

import org.identityshelf.core.domain.IdentifierType;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IdentifierTypeService {
    
    public List<IdentifierType> getAllIdentifierTypes() {
        // TODO: Implement
        return List.of();
    }
    
    public List<IdentifierType> getSearchableIdentifierTypes() {
        // TODO: Implement
        return List.of();
    }
    
    public IdentifierType getIdentifierType(String id) {
        // TODO: Implement
        return null;
    }
    
    public IdentifierType getIdentifierTypeByName(String name) {
        // TODO: Implement
        return null;
    }
    
    public IdentifierType createIdentifierType(Object request) {
        // TODO: Implement
        return null;
    }
    
    public IdentifierType updateIdentifierType(String id, Object request) {
        // TODO: Implement
        return null;
    }
    
    public void deleteIdentifierType(String id) {
        // TODO: Implement
    }
    
    public void activateIdentifierType(String id) {
        // TODO: Implement
    }
}
