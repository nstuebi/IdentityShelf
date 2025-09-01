package org.identityshelf.identity.service;

import org.identityshelf.identity.domain.AttributeType;
import org.identityshelf.identity.domain.AttributeDataType;
import org.identityshelf.identity.repository.AttributeTypeRepository;
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

    public AttributeTypeService(AttributeTypeRepository attributeTypeRepository) {
        this.attributeTypeRepository = attributeTypeRepository;
    }
    
    public List<AttributeType> getAllAttributeTypes() {
        logger.debug("Fetching all attribute types");
        return attributeTypeRepository.findByActiveTrueOrderByName();
    }
    
    public AttributeType getAttributeType(String id) {
        logger.debug("Fetching attribute type by ID: {}", id);
        return attributeTypeRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new RuntimeException("Attribute type not found: " + id));
    }
    
    public AttributeType createAttributeType(Map<String, Object> request) {
        logger.debug("Creating attribute type: {}", request);
        
        // Extract values from request
        String name = (String) request.get("name");
        String displayName = (String) request.get("displayName");
        String description = (String) request.get("description");
        String dataType = (String) request.get("dataType");
        String defaultValue = (String) request.get("defaultValue");
        String validationRegex = (String) request.get("validationRegex");
        Boolean active = (Boolean) request.get("active");
        
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
        
        // Create new independent attribute type
        AttributeType attributeType = new AttributeType();
        attributeType.setName(name);
        attributeType.setDisplayName(displayName);
        attributeType.setDescription(description);
        attributeType.setDataType(AttributeDataType.valueOf(dataType.toUpperCase()));
        attributeType.setDefaultValue(defaultValue);
        attributeType.setValidationRegex(validationRegex);
        attributeType.setActive(active != null ? active : true);
        // Note: createdAt and updatedAt are automatically set by JPA lifecycle methods
        
        logger.info("Created independent attribute type: {}", name);
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

        if (request.containsKey("defaultValue")) {
            existingAttribute.setDefaultValue((String) request.get("defaultValue"));
        }
        if (request.containsKey("validationRegex")) {
            existingAttribute.setValidationRegex((String) request.get("validationRegex"));
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
        
        attributeTypeRepository.deleteById(UUID.fromString(id));
        logger.info("Deleted attribute type: {}", id);
    }
    

}
