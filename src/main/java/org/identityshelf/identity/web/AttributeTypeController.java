package org.identityshelf.identity.web;

import org.identityshelf.identity.domain.AttributeType;
import org.identityshelf.identity.service.AttributeTypeService;
import org.identityshelf.identity.web.dto.AttributeTypeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attribute-types")
public class AttributeTypeController {
    
    private static final Logger logger = LoggerFactory.getLogger(AttributeTypeController.class);
    
    private final AttributeTypeService attributeTypeService;
    
    public AttributeTypeController(AttributeTypeService attributeTypeService) {
        this.attributeTypeService = attributeTypeService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AttributeTypeResponse> getAttributeType(@PathVariable String id) {
        logger.info("Fetching attribute type: {}", id);
        try {
            AttributeType attributeType = attributeTypeService.getAttributeType(id);
            AttributeTypeResponse response = toResponse(attributeType);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching attribute type: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<AttributeTypeResponse> createAttributeType(@RequestBody Map<String, Object> request) {
        logger.info("Creating attribute type: {}", request);
        try {
            AttributeType attributeType = attributeTypeService.createAttributeType(request);
            AttributeTypeResponse response = toResponse(attributeType);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating attribute type", e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AttributeTypeResponse> updateAttributeType(@PathVariable String id, @RequestBody Map<String, Object> request) {
        logger.info("Updating attribute type: {}", id);
        try {
            AttributeType attributeType = attributeTypeService.updateAttributeType(id, request);
            AttributeTypeResponse response = toResponse(attributeType);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating attribute type: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttributeType(@PathVariable String id) {
        logger.info("Deleting attribute type: {}", id);
        try {
            attributeTypeService.deleteAttributeType(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting attribute type: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/by-identity-type/{identityTypeId}")
    public ResponseEntity<List<AttributeTypeResponse>> getAttributesByIdentityType(@PathVariable String identityTypeId) {
        logger.info("Fetching attributes for identity type: {}", identityTypeId);
        try {
            List<AttributeType> attributes = attributeTypeService.getAttributesByIdentityType(identityTypeId);
            List<AttributeTypeResponse> responses = attributes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error fetching attributes for identity type: {}", identityTypeId, e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    private AttributeTypeResponse toResponse(AttributeType attributeType) {
        return new AttributeTypeResponse(
            attributeType.getId(),
            attributeType.getName(),
            attributeType.getDisplayName(),
            attributeType.getDescription(),
            attributeType.getDataType().toString(),
            attributeType.isRequired(),
            attributeType.getDefaultValue(),
            attributeType.getValidationRegex(),
            attributeType.getSortOrder(),
            attributeType.isActive(),
            attributeType.getCreatedAt(),
            attributeType.getUpdatedAt()
        );
    }
}
