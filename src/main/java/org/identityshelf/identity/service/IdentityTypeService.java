package org.identityshelf.identity.service;

import org.identityshelf.identity.domain.IdentityType;
import org.identityshelf.identity.domain.AttributeType;
import org.identityshelf.identity.repository.IdentityTypeRepository;
import org.identityshelf.identity.repository.AttributeTypeRepository;
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
        private final AttributeTypeRepository attributeTypeRepository;

    public IdentityTypeService(
        IdentityTypeRepository identityTypeRepository,
        AttributeTypeRepository attributeTypeRepository
    ) {
        this.identityTypeRepository = identityTypeRepository;
        this.attributeTypeRepository = attributeTypeRepository;
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
        List<AttributeType> attributes = attributeTypeRepository
            .findByIdentityTypeNameAndActiveTrueOrderBySortOrder(typeName);
        return attributes.stream()
            .map(this::toAttributeResponse)
            .collect(Collectors.toList());
    }
    
    private IdentityTypeResponse toResponse(IdentityType type) {
        List<AttributeTypeResponse> attributes = type.getAttributes().stream()
            .filter(AttributeType::isActive)
            .map(this::toAttributeResponse)
            .collect(Collectors.toList());
            
        return new IdentityTypeResponse(
            type.getId(),
            type.getName(),
            type.getDisplayName(),
            type.getDescription(),
            type.isActive(),
            type.getCreatedAt(),
            type.getUpdatedAt(),
            attributes
        );
    }
    
    private AttributeTypeResponse toAttributeResponse(AttributeType attribute) {
        return new AttributeTypeResponse(
            attribute.getId(),
            attribute.getName(),
            attribute.getDisplayName(),
            attribute.getDescription(),
            attribute.getDataType().name(),
            attribute.isRequired(),
            attribute.getDefaultValue(),
            attribute.getValidationRegex(),
            attribute.getSortOrder(),
            attribute.isActive(),
            attribute.getCreatedAt(),
            attribute.getUpdatedAt()
        );
    }
}
