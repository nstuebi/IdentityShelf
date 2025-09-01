package org.identityshelf.identity.service;

import org.identityshelf.identity.domain.IdentityType;
import org.identityshelf.identity.domain.IdentityTypeAttributeMapping;
import org.identityshelf.identity.repository.IdentityTypeRepository;
import org.identityshelf.identity.repository.IdentityTypeAttributeMappingRepository;
import org.identityshelf.identity.web.dto.IdentityTypeResponse;
import org.identityshelf.identity.web.dto.AttributeTypeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IdentityTypeService {
    
    private static final Logger logger = LoggerFactory.getLogger(IdentityTypeService.class);
    
    private final IdentityTypeRepository identityTypeRepository;
    private final IdentityTypeAttributeMappingRepository mappingRepository;

    public IdentityTypeService(
        IdentityTypeRepository identityTypeRepository,
        IdentityTypeAttributeMappingRepository mappingRepository
    ) {
        this.identityTypeRepository = identityTypeRepository;
        this.mappingRepository = mappingRepository;
    }
    
    @Transactional(readOnly = true)
    public List<IdentityTypeResponse> getAllActiveTypes() {
        logger.debug("Fetching all active identity types");
        List<IdentityType> types = identityTypeRepository.findActiveTypesWithAttributes();
        return types.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public IdentityTypeResponse getTypeByName(String typeName) {
        logger.debug("Fetching identity type by name: {}", typeName);
        IdentityType type = identityTypeRepository.findByName(typeName)
            .orElseThrow(() -> new RuntimeException("Identity type not found: " + typeName));
        return toResponse(type);
    }
    
    @Transactional(readOnly = true)
    public List<AttributeTypeResponse> getAttributesForType(String typeName) {
        logger.debug("Fetching attributes for identity type: {}", typeName);
        IdentityType type = identityTypeRepository.findByName(typeName)
            .orElseThrow(() -> new RuntimeException("Identity type not found: " + typeName));
        
        List<IdentityTypeAttributeMapping> mappings = mappingRepository
            .findActiveByIdentityTypeWithAttributeType(type.getUuid());
        
        return mappings.stream()
            .map(this::toAttributeResponseFromMapping)
            .collect(Collectors.toList());
    }
    
    private IdentityTypeResponse toResponse(IdentityType type) {
        // Get mappings for this identity type
        List<IdentityTypeAttributeMapping> mappings = mappingRepository
            .findActiveByIdentityTypeWithAttributeType(type.getUuid());
        
        List<AttributeTypeResponse> attributes = mappings.stream()
            .map(this::toAttributeResponseFromMapping)
            .collect(Collectors.toList());
            
        return new IdentityTypeResponse(
            type.getUuid().toString(),
            type.getName(),
            type.getDisplayName(),
            type.getDescription(),
            type.isActive(),
            type.getCreatedAt(),
            type.getUpdatedAt(),
            attributes
        );
    }
    
    private AttributeTypeResponse toAttributeResponseFromMapping(IdentityTypeAttributeMapping mapping) {
        return new AttributeTypeResponse(
            mapping.getAttributeType().getUuid().toString(),
            mapping.getAttributeType().getName(),
            mapping.getAttributeType().getDisplayName(),
            mapping.getAttributeType().getDescription(),
            mapping.getAttributeType().getDataType().name(),
            mapping.isRequired(), // From mapping
            mapping.getEffectiveDefaultValue(), // Effective value with overrides
            mapping.getEffectiveValidationRegex(), // Effective regex with overrides
            mapping.getSortOrder(), // From mapping
            mapping.isActive(),
            mapping.getAttributeType().getCreatedAt(),
            mapping.getAttributeType().getUpdatedAt()
        );
    }
}
