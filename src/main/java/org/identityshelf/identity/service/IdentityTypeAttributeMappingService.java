package org.identityshelf.identity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identityshelf.identity.domain.AttributeType;
import org.identityshelf.identity.domain.IdentityType;
import org.identityshelf.identity.domain.IdentityTypeAttributeMapping;
import org.identityshelf.identity.repository.AttributeTypeRepository;
import org.identityshelf.identity.repository.IdentityTypeAttributeMappingRepository;
import org.identityshelf.identity.repository.IdentityTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class IdentityTypeAttributeMappingService {
    
    private final IdentityTypeAttributeMappingRepository mappingRepository;
    private final IdentityTypeRepository identityTypeRepository;
    private final AttributeTypeRepository attributeTypeRepository;
    
    public List<IdentityTypeAttributeMapping> getMappingsForIdentityType(String identityTypeId) {
        return mappingRepository.findActiveByIdentityTypeWithAttributeType(UUID.fromString(identityTypeId));
    }
    
    public List<IdentityTypeAttributeMapping> getMappingsForAttributeType(String attributeTypeId) {
        return mappingRepository.findByAttributeTypeUuidAndActiveTrue(UUID.fromString(attributeTypeId));
    }
    
    public Optional<IdentityTypeAttributeMapping> getMapping(String identityTypeId, String attributeTypeId) {
        return mappingRepository.findByIdentityTypeUuidAndAttributeTypeUuidAndActiveTrue(UUID.fromString(identityTypeId), UUID.fromString(attributeTypeId));
    }
    
    public IdentityTypeAttributeMapping getMappingById(String mappingId) {
        return mappingRepository.findById(UUID.fromString(mappingId))
                .orElseThrow(() -> new IllegalArgumentException("Mapping not found: " + mappingId));
    }
    
    public IdentityTypeAttributeMapping createMapping(String identityTypeId, String attributeTypeId, 
                                                     int sortOrder, boolean required, 
                                                     String overrideValidationRegex, String overrideDefaultValue) {
        
        IdentityType identityType = identityTypeRepository.findById(UUID.fromString(identityTypeId))
                .orElseThrow(() -> new IllegalArgumentException("Identity type not found: " + identityTypeId));
        
        AttributeType attributeType = attributeTypeRepository.findById(UUID.fromString(attributeTypeId))
                .orElseThrow(() -> new IllegalArgumentException("Attribute type not found: " + attributeTypeId));
        
        // Check if mapping already exists
        if (mappingRepository.existsByIdentityTypeUuidAndAttributeTypeUuidAndActiveTrue(UUID.fromString(identityTypeId), UUID.fromString(attributeTypeId))) {
            throw new IllegalArgumentException("Mapping already exists between identity type and attribute type");
        }
        
        IdentityTypeAttributeMapping mapping = new IdentityTypeAttributeMapping();
        mapping.setIdentityType(identityType);
        mapping.setAttributeType(attributeType);
        mapping.setSortOrder(sortOrder);
        mapping.setRequired(required);
        mapping.setOverrideValidationRegex(overrideValidationRegex);
        mapping.setOverrideDefaultValue(overrideDefaultValue);
        
        log.debug("Creating mapping between identity type {} and attribute type {}", 
                 identityTypeId, attributeTypeId);
        
        return mappingRepository.save(mapping);
    }
    
    public IdentityTypeAttributeMapping updateMapping(String mappingId, int sortOrder, boolean required, 
                                                     String overrideValidationRegex, String overrideDefaultValue) {
        
        IdentityTypeAttributeMapping mapping = mappingRepository.findById(UUID.fromString(mappingId))
                .orElseThrow(() -> new IllegalArgumentException("Mapping not found: " + mappingId));
        
        mapping.setSortOrder(sortOrder);
        mapping.setRequired(required);
        mapping.setOverrideValidationRegex(overrideValidationRegex);
        mapping.setOverrideDefaultValue(overrideDefaultValue);
        
        log.debug("Updating mapping {}", mappingId);
        
        return mappingRepository.save(mapping);
    }
    
    public void deleteMapping(String mappingId) {
        IdentityTypeAttributeMapping mapping = mappingRepository.findById(UUID.fromString(mappingId))
                .orElseThrow(() -> new IllegalArgumentException("Mapping not found: " + mappingId));
        
        mapping.setActive(false);
        mappingRepository.save(mapping);
        
        log.debug("Deactivated mapping {}", mappingId);
    }
    
    public void deleteMappingPermanently(String mappingId) {
        mappingRepository.deleteById(UUID.fromString(mappingId));
        log.debug("Permanently deleted mapping {}", mappingId);
    }
    
    /**
     * Validates a value against both base attribute rules and mapping-specific overrides
     */
    public boolean validateValueForMapping(String mappingId, String value) {
        IdentityTypeAttributeMapping mapping = mappingRepository.findById(UUID.fromString(mappingId))
                .orElseThrow(() -> new IllegalArgumentException("Mapping not found: " + mappingId));
        
        return mapping.validateValue(value);
    }
    
    /**
     * Gets the effective validation regex considering both base and override
     */
    public String getEffectiveValidationRegex(String mappingId) {
        IdentityTypeAttributeMapping mapping = mappingRepository.findById(UUID.fromString(mappingId))
                .orElseThrow(() -> new IllegalArgumentException("Mapping not found: " + mappingId));
        
        return mapping.getEffectiveValidationRegex();
    }
    
    /**
     * Gets the effective default value considering both base and override
     */
    public String getEffectiveDefaultValue(String mappingId) {
        IdentityTypeAttributeMapping mapping = mappingRepository.findById(UUID.fromString(mappingId))
                .orElseThrow(() -> new IllegalArgumentException("Mapping not found: " + mappingId));
        
        return mapping.getEffectiveDefaultValue();
    }
}
