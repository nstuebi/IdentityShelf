package org.identityshelf.identity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identityshelf.identity.domain.AttributeDataType;
import org.identityshelf.identity.domain.IdentifierType;
import org.identityshelf.identity.repository.IdentifierTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdentifierTypeService {

    private final IdentifierTypeRepository identifierTypeRepository;

    @Transactional(readOnly = true)
    public List<IdentifierType> getAllIdentifierTypes() {
        return identifierTypeRepository.findByActiveTrueOrderByDisplayName();
    }

    @Transactional(readOnly = true)
    public List<IdentifierType> getSearchableIdentifierTypes() {
        return identifierTypeRepository.findActiveSearchableTypes();
    }

    @Transactional(readOnly = true)
    public IdentifierType getIdentifierType(String id) {
        return identifierTypeRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Identifier type not found"));
    }

    @Transactional(readOnly = true)
    public IdentifierType getIdentifierTypeByName(String name) {
        return identifierTypeRepository.findByNameAndActiveTrue(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Identifier type not found: " + name));
    }

    @Transactional
    public IdentifierType createIdentifierType(String name, String displayName, String description,
                                              AttributeDataType dataType, String validationRegex,
                                              String defaultValue, boolean unique, boolean searchable) {
        // Check if name already exists
        if (identifierTypeRepository.existsByName(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Identifier type with name '" + name + "' already exists");
        }

        IdentifierType identifierType = new IdentifierType();
        identifierType.setName(name);
        identifierType.setDisplayName(displayName);
        identifierType.setDescription(description);
        identifierType.setDataType(dataType);
        identifierType.setValidationRegex(validationRegex);
        identifierType.setDefaultValue(defaultValue);
        identifierType.setUnique(unique);
        identifierType.setSearchable(searchable);
        identifierType.setActive(true);

        IdentifierType saved = identifierTypeRepository.save(identifierType);
        log.info("Created identifier type: {} ({})", saved.getDisplayName(), saved.getName());
        return saved;
    }

    @Transactional
    public IdentifierType updateIdentifierType(String id, String name, String displayName, String description,
                                              AttributeDataType dataType, String validationRegex,
                                              String defaultValue, boolean unique, boolean searchable) {
        IdentifierType identifierType = getIdentifierType(id);

        // Check if name is being changed and if new name already exists
        if (!identifierType.getName().equals(name) && identifierTypeRepository.existsByName(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Identifier type with name '" + name + "' already exists");
        }

        identifierType.setName(name);
        identifierType.setDisplayName(displayName);
        identifierType.setDescription(description);
        identifierType.setDataType(dataType);
        identifierType.setValidationRegex(validationRegex);
        identifierType.setDefaultValue(defaultValue);
        identifierType.setUnique(unique);
        identifierType.setSearchable(searchable);

        IdentifierType saved = identifierTypeRepository.save(identifierType);
        log.info("Updated identifier type: {} ({})", saved.getDisplayName(), saved.getName());
        return saved;
    }

    @Transactional
    public void deleteIdentifierType(String id) {
        IdentifierType identifierType = getIdentifierType(id);
        
        // Soft delete by marking as inactive
        identifierType.setActive(false);
        identifierTypeRepository.save(identifierType);
        
        log.info("Deactivated identifier type: {} ({})", identifierType.getDisplayName(), identifierType.getName());
    }

    @Transactional
    public void activateIdentifierType(String id) {
        IdentifierType identifierType = identifierTypeRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Identifier type not found"));
        
        identifierType.setActive(true);
        identifierTypeRepository.save(identifierType);
        
        log.info("Activated identifier type: {} ({})", identifierType.getDisplayName(), identifierType.getName());
    }
}
