package org.identityshelf.identity.service;

import java.util.UUID;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.identityshelf.identity.domain.Identity;
import org.identityshelf.identity.domain.IdentityType;
import org.identityshelf.identity.domain.IdentityTypeAttributeMapping;
import org.identityshelf.identity.domain.IdentityAttributeValue;
import org.identityshelf.identity.domain.IdentityStatus;
import org.identityshelf.identity.repository.IdentityRepository;
import org.identityshelf.identity.repository.IdentityTypeRepository;
import org.identityshelf.identity.repository.IdentityTypeAttributeMappingRepository;
import org.identityshelf.identity.web.dto.CreateIdentityRequest;
import org.identityshelf.identity.web.dto.IdentityResponse;
import org.identityshelf.identity.web.dto.UpdateIdentityRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.identityshelf.identity.service.IdentityValidationService;
import org.identityshelf.identity.service.IdentityValidationService.ValidationResult;
import org.identityshelf.identity.service.IdentityValidationService.ValidationError;
import org.identityshelf.identity.exception.ValidationException;

@Service
@Transactional
public class IdentityService {

    private static final Logger logger = LoggerFactory.getLogger(IdentityService.class);

    private final IdentityRepository identityRepository;
    private final IdentityTypeRepository identityTypeRepository;
    private final IdentityTypeAttributeMappingRepository mappingRepository;
    private final IdentityValidationService validationService;

    public IdentityService(
        IdentityRepository identityRepository,
        IdentityTypeRepository identityTypeRepository,
        IdentityTypeAttributeMappingRepository mappingRepository,
        IdentityValidationService validationService
    ) {
        this.identityRepository = identityRepository;
        this.identityTypeRepository = identityTypeRepository;
        this.mappingRepository = mappingRepository;
        this.validationService = validationService;
    }

    public IdentityResponse createIdentity(CreateIdentityRequest request) {
        logger.debug("Creating identity with type: {}", request.getIdentityType());
        
        try {
            // Get the identity type
            IdentityType identityType = identityTypeRepository.findById(UUID.fromString(request.getIdentityType()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid identity type ID: " + request.getIdentityType()));

            // Get attribute mappings for validation
            List<IdentityTypeAttributeMapping> mappings = mappingRepository
                .findActiveByIdentityTypeWithAttributeType(identityType.getUuid());
            
            // Validate the request
            ValidationResult validationResult = validationService.validateIdentity(request, mappings);
            if (!validationResult.isValid()) {
                logger.warn("Validation failed for identity creation: {}", validationResult.getErrors());
                throw new ValidationException("Validation failed", validationResult.getErrors());
            }

            // Extract core attributes from the attributes map
            Map<String, Object> attributes = request.getAttributes() != null ? request.getAttributes() : new HashMap<>();
            String displayName = (String) attributes.get("display_name");
            String statusStr = (String) attributes.get("status");

            Identity identity = new Identity();
            identity.setDisplayName(displayName != null ? displayName : "New Identity");
            if (statusStr != null) {
                try {
                    identity.setStatus(IdentityStatus.valueOf(statusStr.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    logger.warn("Invalid status value: {}, using default ACTIVE", statusStr);
                    identity.setStatus(IdentityStatus.ACTIVE);
                }
            }
            identity.setIdentityType(identityType);

            // Create and set attribute values based on mappings
            for (IdentityTypeAttributeMapping mapping : mappings) {
                Object value = attributes.get(mapping.getAttributeType().getName());
                if (value != null || mapping.isRequired()) {
                    IdentityAttributeValue identityValue = new IdentityAttributeValue(identity, mapping.getAttributeType());
                    identityValue.setValue(value != null ? value : mapping.getEffectiveDefaultValue());
                    identity.addValue(identityValue);
                    logger.debug("Added attribute value: {} = {} (AttributeType: {})", 
                        mapping.getAttributeType().getName(), value, mapping.getAttributeType().getUuid());
                }
            }

            logger.debug("Saving identity: {}", identity);
            Identity saved = identityRepository.save(identity);
            logger.info("Successfully created identity with ID: {}", saved.getUuid());
            return toResponse(saved);
        } catch (Exception e) {
            logger.error("Error creating identity: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Page<IdentityResponse> listIdentities(Pageable pageable) {
        return identityRepository.findAll(pageable).map(this::toResponse);
    }

    private void updateIdentityAttributes(Identity identity, Map<String, Object> attributesMap, 
                                        List<IdentityTypeAttributeMapping> mappings) {
        logger.debug("Updating attributes for identity: {}", identity.getUuid());
        
        // Remove existing attribute values
        identity.getValues().clear();
        
        // Add new attribute values
        for (IdentityTypeAttributeMapping mapping : mappings) {
            String attributeName = mapping.getAttributeType().getName();
            Object value = attributesMap.get(attributeName);
            
            if (value != null) {
                IdentityAttributeValue attributeValue = new IdentityAttributeValue();
                attributeValue.setIdentity(identity);
                attributeValue.setAttributeType(mapping.getAttributeType());
                attributeValue.setValue(String.valueOf(value));
                
                identity.getValues().add(attributeValue);
                logger.debug("Updated attribute '{}' with value: {}", attributeName, value);
            }
        }
    }

    @Transactional(readOnly = true)
    public IdentityResponse getIdentity(UUID id) {
        Identity identity = identityRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Identity not found"));
        return toResponse(identity);
    }

    public IdentityResponse updateIdentity(UUID id, UpdateIdentityRequest request) {
        logger.debug("Updating identity with id: {}", id);
        
        try {
            Identity identity = identityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Identity not found"));

            // Get attribute mappings for validation
            List<IdentityTypeAttributeMapping> mappings = mappingRepository
                .findActiveByIdentityTypeWithAttributeType(identity.getIdentityType().getUuid());
            
            // Validate the request if attributes are provided
            if (request.getAttributes() != null && !request.getAttributes().isEmpty()) {
                ValidationResult validationResult = validationService.validateIdentityUpdate(request, mappings);
                if (!validationResult.isValid()) {
                    logger.warn("Validation failed for identity update: {}", validationResult.getErrors());
                    throw new ValidationException("Validation failed", validationResult.getErrors());
                }
            }

            // Update basic fields
            if (request.getDisplayName() != null) {
                identity.setDisplayName(request.getDisplayName());
            }
            if (request.getStatus() != null) {
                try {
                    identity.setStatus(IdentityStatus.valueOf(request.getStatus().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status value: " + request.getStatus());
                }
            }

            // Update attributes if provided
            if (request.getAttributes() != null) {
                updateIdentityAttributes(identity, request.getAttributes(), mappings);
            }

            Identity saved = identityRepository.save(identity);
            logger.info("Successfully updated identity: {}", id);
            return toResponse(saved);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error updating identity {}: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update identity: " + e.getMessage());
        }
    }

    public void deleteIdentity(UUID id) {
        if (!identityRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Identity not found");
        }
        identityRepository.deleteById(id);
    }

    private IdentityResponse toResponse(Identity identity) {
        logger.debug("Converting identity to response. Values count: {}", identity.getValues().size());
        
        // Convert attributes to a map, filtering out any with null AttributeType or null name
        Map<String, Object> attributesMap = identity.getValues().stream()
            .filter(v -> {
                if (v.getAttributeType() == null) {
                    logger.warn("Found IdentityAttributeValue with null AttributeType: {}", v.getUuid());
                    return false;
                }
                if (v.getAttributeType().getName() == null) {
                    logger.warn("Found AttributeType with null name: {}", v.getAttributeType().getUuid());
                    return false;
                }
                return true;
            })
            .filter(v -> v.getAttributeType().getName() != null && !v.getAttributeType().getName().trim().isEmpty())
            .collect(Collectors.toMap(
                v -> v.getAttributeType().getName(),
                v -> v.getValue() != null ? v.getValue() : ""
            ));
        
        logger.debug("Created attributes map with {} entries", attributesMap.size());
            
        return new IdentityResponse(
            identity.getUuid(),
            identity.getDisplayName(),
            identity.getStatus().toString(),
            identity.getCreatedAt(),
            identity.getUpdatedAt(),
            identity.getIdentityType() != null ? identity.getIdentityType().getName() : "DEFAULT",
            attributesMap
        );
    }
}


