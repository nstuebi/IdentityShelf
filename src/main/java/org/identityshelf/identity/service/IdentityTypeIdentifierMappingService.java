package org.identityshelf.identity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identityshelf.identity.domain.IdentifierType;
import org.identityshelf.identity.domain.IdentityType;
import org.identityshelf.identity.domain.IdentityTypeIdentifierMapping;
import org.identityshelf.identity.repository.IdentityTypeIdentifierMappingRepository;
import org.identityshelf.identity.repository.IdentityTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdentityTypeIdentifierMappingService {

    private final IdentityTypeIdentifierMappingRepository mappingRepository;
    private final IdentityTypeRepository identityTypeRepository;
    private final IdentifierTypeService identifierTypeService;

    @Transactional(readOnly = true)
    public List<IdentityTypeIdentifierMapping> getMappingsForIdentityType(String identityTypeId) {
        return mappingRepository.findActiveByIdentityTypeWithIdentifierType(UUID.fromString(identityTypeId));
    }

    @Transactional(readOnly = true)
    public List<IdentityTypeIdentifierMapping> getMappingsForIdentifierType(String identifierTypeId) {
        return mappingRepository.findByIdentifierTypeUuidAndActiveTrue(UUID.fromString(identifierTypeId));
    }

    @Transactional(readOnly = true)
    public Optional<IdentityTypeIdentifierMapping> getMapping(String identityTypeId, String identifierTypeId) {
        return mappingRepository.findByIdentityTypeUuidAndIdentifierTypeUuidAndActiveTrue(
                UUID.fromString(identityTypeId), UUID.fromString(identifierTypeId));
    }

    @Transactional(readOnly = true)
    public IdentityTypeIdentifierMapping getMappingById(String mappingId) {
        return mappingRepository.findById(UUID.fromString(mappingId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mapping not found"));
    }

    @Transactional(readOnly = true)
    public IdentityTypeIdentifierMapping getMappingById(UUID mappingId) {
        return mappingRepository.findById(mappingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mapping not found"));
    }

    @Transactional(readOnly = true)
    public List<IdentityTypeIdentifierMapping> getAllActiveMappings() {
        return mappingRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<IdentityTypeIdentifierMapping> getMappingsByIdentityType(UUID identityTypeId) {
        return mappingRepository.findActiveByIdentityTypeWithIdentifierType(identityTypeId);
    }

    @Transactional(readOnly = true)
    public List<IdentityTypeIdentifierMapping> getRequiredIdentifiersForIdentityType(String identityTypeId) {
        return mappingRepository.findRequiredByIdentityType(UUID.fromString(identityTypeId));
    }

    @Transactional(readOnly = true)
    public List<IdentityTypeIdentifierMapping> getPrimaryCandidatesForIdentityType(String identityTypeId) {
        return mappingRepository.findPrimaryCandidatesByIdentityType(UUID.fromString(identityTypeId));
    }

    @Transactional
    public IdentityTypeIdentifierMapping createMapping(UUID identityTypeId, UUID identifierTypeId, 
                                                      int sortOrder, boolean required, boolean primaryCandidate,
                                                      String overrideValidationRegex, String overrideDefaultValue) {
        // Validate that identity type exists
        IdentityType identityType = identityTypeRepository.findById(identityTypeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid identity type ID: " + identityTypeId));

        // Validate that identifier type exists
        IdentifierType identifierType = identifierTypeService.getIdentifierType(identifierTypeId.toString());

        // Check if mapping already exists
        if (mappingRepository.existsByIdentityTypeUuidAndIdentifierTypeUuidAndActiveTrue(identityTypeId, identifierTypeId)) {
            throw new IllegalArgumentException("Mapping already exists between identity type and identifier type");
        }

        IdentityTypeIdentifierMapping mapping = IdentityTypeIdentifierMapping.builder()
                .identityType(identityType)
                .identifierType(identifierType)
                .sortOrder(sortOrder)
                .required(required)
                .primaryCandidate(primaryCandidate)
                .overrideValidationRegex(overrideValidationRegex)
                .overrideDefaultValue(overrideDefaultValue)
                .active(true)
                .build();

        IdentityTypeIdentifierMapping saved = mappingRepository.save(mapping);
        log.info("Created identifier mapping between identity type '{}' and identifier type '{}'", 
                identityType.getName(), identifierType.getName());
        
        return saved;
    }

    @Transactional
    public IdentityTypeIdentifierMapping createMapping(String identityTypeId, String identifierTypeId, 
                                                      int sortOrder, boolean required, boolean primaryCandidate,
                                                      String overrideValidationRegex, String overrideDefaultValue) {
        // Validate that identity type exists
        IdentityType identityType = identityTypeRepository.findById(UUID.fromString(identityTypeId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid identity type ID: " + identityTypeId));

        // Validate that identifier type exists
        IdentifierType identifierType = identifierTypeService.getIdentifierType(identifierTypeId);

        // Check if mapping already exists
        if (mappingRepository.existsByIdentityTypeUuidAndIdentifierTypeUuidAndActiveTrue(
                UUID.fromString(identityTypeId), UUID.fromString(identifierTypeId))) {
            throw new IllegalArgumentException("Mapping already exists between identity type and identifier type");
        }

        IdentityTypeIdentifierMapping mapping = IdentityTypeIdentifierMapping.builder()
                .identityType(identityType)
                .identifierType(identifierType)
                .sortOrder(sortOrder)
                .required(required)
                .primaryCandidate(primaryCandidate)
                .overrideValidationRegex(overrideValidationRegex)
                .overrideDefaultValue(overrideDefaultValue)
                .active(true)
                .build();

        IdentityTypeIdentifierMapping saved = mappingRepository.save(mapping);
        log.info("Created identifier mapping between identity type '{}' and identifier type '{}'", 
                identityType.getName(), identifierType.getName());
        
        return saved;
    }

    @Transactional
    public IdentityTypeIdentifierMapping updateMapping(UUID mappingId, int sortOrder, boolean required, 
                                                       boolean primaryCandidate, String overrideValidationRegex, 
                                                       String overrideDefaultValue) {
        IdentityTypeIdentifierMapping mapping = getMappingById(mappingId);
        
        mapping.setSortOrder(sortOrder);
        mapping.setRequired(required);
        mapping.setPrimaryCandidate(primaryCandidate);
        mapping.setOverrideValidationRegex(overrideValidationRegex);
        mapping.setOverrideDefaultValue(overrideDefaultValue);

        IdentityTypeIdentifierMapping saved = mappingRepository.save(mapping);
        log.info("Updated identifier mapping: {}", mappingId);
        
        return saved;
    }

    @Transactional
    public IdentityTypeIdentifierMapping updateMapping(String mappingId, int sortOrder, boolean required, 
                                                       boolean primaryCandidate, String overrideValidationRegex, 
                                                       String overrideDefaultValue) {
        IdentityTypeIdentifierMapping mapping = getMappingById(mappingId);
        
        mapping.setSortOrder(sortOrder);
        mapping.setRequired(required);
        mapping.setPrimaryCandidate(primaryCandidate);
        mapping.setOverrideValidationRegex(overrideValidationRegex);
        mapping.setOverrideDefaultValue(overrideDefaultValue);

        IdentityTypeIdentifierMapping saved = mappingRepository.save(mapping);
        log.info("Updated identifier mapping: {}", mappingId);
        
        return saved;
    }

    @Transactional
    public void deactivateMapping(UUID mappingId) {
        IdentityTypeIdentifierMapping mapping = getMappingById(mappingId);
        
        // Soft delete by marking as inactive
        mapping.setActive(false);
        mappingRepository.save(mapping);
        
        log.info("Deactivated identifier mapping: {}", mappingId);
    }

    @Transactional
    public IdentityTypeIdentifierMapping activateMapping(UUID mappingId) {
        IdentityTypeIdentifierMapping mapping = getMappingById(mappingId);
        
        mapping.setActive(true);
        IdentityTypeIdentifierMapping saved = mappingRepository.save(mapping);
        
        log.info("Activated identifier mapping: {}", mappingId);
        return saved;
    }

    @Transactional
    public void deleteMapping(String mappingId) {
        IdentityTypeIdentifierMapping mapping = getMappingById(mappingId);
        
        // Soft delete by marking as inactive
        mapping.setActive(false);
        mappingRepository.save(mapping);
        
        log.info("Deactivated identifier mapping: {}", mappingId);
    }

    @Transactional
    public void reorderMappings(String identityTypeId, List<String> mappingIds) {
        List<IdentityTypeIdentifierMapping> mappings = getMappingsForIdentityType(identityTypeId);
        
        for (int i = 0; i < mappingIds.size(); i++) {
            final int sortOrder = i; // Make variable effectively final
            String mappingId = mappingIds.get(i);
            mappings.stream()
                    .filter(m -> m.getUuid().toString().equals(mappingId))
                    .findFirst()
                    .ifPresent(mapping -> {
                        mapping.setSortOrder(sortOrder);
                        mappingRepository.save(mapping);
                    });
        }
        
        log.info("Reordered identifier mappings for identity type: {}", identityTypeId);
    }
}
