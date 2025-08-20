package org.identityshelf.identity.service;

import org.identityshelf.identity.domain.AttributeType;
import org.identityshelf.identity.domain.AttributeDataType;
import org.identityshelf.identity.domain.IdentityType;
import org.identityshelf.identity.repository.AttributeTypeRepository;
import org.identityshelf.identity.repository.IdentityTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class AttributeTypeService {
    
    private static final Logger logger = LoggerFactory.getLogger(AttributeTypeService.class);
    
    private final AttributeTypeRepository attributeTypeRepository;
    private final IdentityTypeRepository identityTypeRepository;
    
    public AttributeTypeService(AttributeTypeRepository attributeTypeRepository, 
                               IdentityTypeRepository identityTypeRepository) {
        this.attributeTypeRepository = attributeTypeRepository;
        this.identityTypeRepository = identityTypeRepository;
    }
    
    public AttributeType getAttributeType(String id) {
        logger.debug("Fetching attribute type by ID: {}", id);
        return attributeTypeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Attribute type not found: " + id));
    }
    
    public AttributeType createAttributeType(Map<String, Object> request) {
        logger.debug("Creating attribute type: {}", request);
        
        // Extract values from request
        String name = (String) request.get("name");
        String displayName = (String) request.get("displayName");
        String description = (String) request.get("description");
        String dataType = (String) request.get("dataType");
        Boolean required = (Boolean) request.get("required");
        String defaultValue = (String) request.get("defaultValue");
        String validationRegex = (String) request.get("validationRegex");
        Integer sortOrder = (Integer) request.get("sortOrder");
        Boolean active = (Boolean) request.get("active");
        String identityTypeId = (String) request.get("identityTypeId");
        
        // Validate required fields
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Attribute name is required");
        }
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new RuntimeException("Display name is required");
        }
        if (dataType == null || dataType.trim().isEmpty()) {
            throw new RuntimeException("Data type is required");
        }
        if (identityTypeId == null || identityTypeId.trim().isEmpty()) {
            throw new RuntimeException("Identity type ID is required");
        }
        
        // Check if identity type exists
        IdentityType identityType = identityTypeRepository.findById(identityTypeId)
            .orElseThrow(() -> new RuntimeException("Identity type not found: " + identityTypeId));
        
        // Check if attribute name already exists for this identity type
        // TODO: Implement proper duplicate check
        // For now, we'll allow duplicates and handle them at the database level
        
        // Create new attribute type
        AttributeType attributeType = new AttributeType();
        attributeType.setName(name);
        attributeType.setDisplayName(displayName);
        attributeType.setDescription(description);
        attributeType.setDataType(AttributeDataType.valueOf(dataType.toUpperCase()));
        attributeType.setRequired(required != null ? required : false);
        attributeType.setDefaultValue(defaultValue);
        attributeType.setValidationRegex(validationRegex);
        attributeType.setSortOrder(sortOrder != null ? sortOrder : 0);
        attributeType.setActive(active != null ? active : true);
        attributeType.setIdentityType(identityType);
        // Note: createdAt and updatedAt are automatically set by JPA lifecycle methods
        
        logger.info("Created attribute type: {} for identity type: {}", name, identityType.getName());
        return attributeTypeRepository.save(attributeType);
    }
    
    public AttributeType updateAttributeType(String id, Map<String, Object> request) {
        logger.debug("Updating attribute type: {} with data: {}", id, request);
        
        AttributeType existingAttribute = getAttributeType(id);
        
        // Update fields if provided
        if (request.containsKey("displayName")) {
            existingAttribute.setDisplayName((String) request.get("displayName"));
        }
        if (request.containsKey("description")) {
            existingAttribute.setDescription((String) request.get("description"));
        }
        if (request.containsKey("dataType")) {
            existingAttribute.setDataType(AttributeDataType.valueOf(((String) request.get("dataType")).toUpperCase()));
        }
        if (request.containsKey("required")) {
            existingAttribute.setRequired((Boolean) request.get("required"));
        }
        if (request.containsKey("defaultValue")) {
            existingAttribute.setDefaultValue((String) request.get("defaultValue"));
        }
        if (request.containsKey("validationRegex")) {
            existingAttribute.setValidationRegex((String) request.get("validationRegex"));
        }
        if (request.containsKey("sortOrder")) {
            existingAttribute.setSortOrder((Integer) request.get("sortOrder"));
        }
        if (request.containsKey("active")) {
            existingAttribute.setActive((Boolean) request.get("active"));
        }
        
        // Note: updatedAt is automatically set by JPA lifecycle methods
        
        logger.info("Updated attribute type: {}", id);
        return attributeTypeRepository.save(existingAttribute);
    }
    
    public void deleteAttributeType(String id) {
        logger.debug("Deleting attribute type: {}", id);
        
        AttributeType attributeType = getAttributeType(id);
        
        // Check if this attribute is used by any identities
        // TODO: Implement check for usage before deletion
        
        attributeTypeRepository.deleteById(id);
        logger.info("Deleted attribute type: {}", id);
    }
    
    public List<AttributeType> getAttributesByIdentityType(String identityTypeId) {
        logger.debug("Fetching attributes for identity type: {}", identityTypeId);
        return attributeTypeRepository.findActiveAttributesByTypeId(identityTypeId);
    }
}
