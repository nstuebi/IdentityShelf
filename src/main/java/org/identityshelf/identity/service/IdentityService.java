package org.identityshelf.identity.service;

import java.util.UUID;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import org.identityshelf.identity.domain.Identity;
import org.identityshelf.identity.domain.IdentityType;
import org.identityshelf.identity.domain.AttributeType;
import org.identityshelf.identity.domain.IdentityAttributeValue;
import org.identityshelf.identity.repository.IdentityRepository;
import org.identityshelf.identity.repository.IdentityTypeRepository;
import org.identityshelf.identity.repository.AttributeTypeRepository;
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
    private final AttributeTypeRepository attributeTypeRepository;
    private final IdentityValidationService validationService;

    public IdentityService(
        IdentityRepository identityRepository,
        IdentityTypeRepository identityTypeRepository,
        AttributeTypeRepository attributeTypeRepository,
        IdentityValidationService validationService
    ) {
        this.identityRepository = identityRepository;
        this.identityTypeRepository = identityTypeRepository;
        this.attributeTypeRepository = attributeTypeRepository;
        this.validationService = validationService;
    }

    public IdentityResponse createIdentity(CreateIdentityRequest request) {
        logger.debug("Creating identity with username: {}, email: {}, type: {}", 
            request.getUsername(), request.getEmail(), request.getIdentityType());
        
        try {
            // Get the identity type
            IdentityType identityType = identityTypeRepository.findById(request.getIdentityType())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid identity type ID: " + request.getIdentityType()));

            // Get attribute types for validation
            List<AttributeType> attributeTypes = attributeTypeRepository
                .findByIdentityTypeIdAndActiveTrueOrderBySortOrder(identityType.getId());
            
            // Validate the request
            ValidationResult validationResult = validationService.validateIdentity(request, attributeTypes);
            if (!validationResult.isValid()) {
                logger.warn("Validation failed for identity creation: {}", validationResult.getErrors());
                throw new ValidationException("Validation failed", validationResult.getErrors());
            }

            if (identityRepository.existsByUsername(request.getUsername())) {
                logger.warn("Username already exists: {}", request.getUsername());
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
            }
            if (identityRepository.existsByEmail(request.getEmail())) {
                logger.warn("Email already exists: {}", request.getEmail());
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
            }

            Identity identity = new Identity();
            identity.setUsername(request.getUsername());
            identity.setEmail(request.getEmail());
            identity.setFirstName(request.getFirstName());
            identity.setLastName(request.getLastName());
            identity.setIdentityType(identityType);

            // Create and set attribute values
            if (request.getAttributes() != null) {
                for (AttributeType attribute : attributeTypes) {
                    Object value = request.getAttributes().get(attribute.getName());
                    if (value != null || attribute.isRequired()) {
                        IdentityAttributeValue identityValue = new IdentityAttributeValue(identity, attribute);
                        identityValue.setValue(value != null ? value : attribute.getDefaultValue());
                        identity.addValue(identityValue);
                    }
                }
            }

            logger.debug("Saving identity: {}", identity);
            Identity saved = identityRepository.save(identity);
            logger.info("Successfully created identity with ID: {}", saved.getId());
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

    @Transactional(readOnly = true)
    public IdentityResponse getIdentity(UUID id) {
        Identity identity = identityRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Identity not found"));
        return toResponse(identity);
    }

    public IdentityResponse updateIdentity(UUID id, UpdateIdentityRequest request) {
        Identity identity = identityRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Identity not found"));

        if (request.getUsername() != null && !request.getUsername().equals(identity.getUsername())) {
            if (identityRepository.existsByUsernameAndIdNot(request.getUsername(), id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
            }
            identity.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().equals(identity.getEmail())) {
            if (identityRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
            }
            identity.setEmail(request.getEmail());
        }

        if (request.getFirstName() != null) {
            identity.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            identity.setLastName(request.getLastName());
        }

        Identity saved = identityRepository.save(identity);
        return toResponse(saved);
    }

    public void deleteIdentity(UUID id) {
        if (!identityRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Identity not found");
        }
        identityRepository.deleteById(id);
    }

    private IdentityResponse toResponse(Identity identity) {
        // Convert attributes to a map
        Map<String, Object> attributesMap = identity.getValues().stream()
            .collect(Collectors.toMap(
                v -> v.getAttributeType().getName(),
                IdentityAttributeValue::getValue
            ));
            
        return new IdentityResponse(
            identity.getId(),
            identity.getUsername(),
            identity.getEmail(),
            identity.getFirstName(),
            identity.getLastName(),
            identity.getCreatedAt(),
            identity.getUpdatedAt(),
            identity.getIdentityType() != null ? identity.getIdentityType().getName() : "DEFAULT",
            attributesMap
        );
    }
}


