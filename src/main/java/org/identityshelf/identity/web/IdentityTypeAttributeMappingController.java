package org.identityshelf.identity.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identityshelf.identity.domain.IdentityTypeAttributeMapping;
import org.identityshelf.identity.service.IdentityTypeAttributeMappingService;
import org.identityshelf.identity.web.dto.CreateMappingRequest;
import org.identityshelf.identity.web.dto.IdentityTypeAttributeMappingResponse;
import org.identityshelf.identity.web.dto.UpdateMappingRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/identity-type-attribute-mappings")
@RequiredArgsConstructor
@Slf4j
public class IdentityTypeAttributeMappingController {
    
    private final IdentityTypeAttributeMappingService mappingService;
    
    @GetMapping("/by-identity-type/{identityTypeId}")
    public List<IdentityTypeAttributeMappingResponse> getMappingsForIdentityType(@PathVariable String identityTypeId) {
        List<IdentityTypeAttributeMapping> mappings = mappingService.getMappingsForIdentityType(identityTypeId);
        return mappings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @GetMapping("/by-attribute-type/{attributeTypeId}")
    public List<IdentityTypeAttributeMappingResponse> getMappingsForAttributeType(@PathVariable String attributeTypeId) {
        List<IdentityTypeAttributeMapping> mappings = mappingService.getMappingsForAttributeType(attributeTypeId);
        return mappings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @GetMapping("/{mappingId}")
    public IdentityTypeAttributeMappingResponse getMapping(@PathVariable String mappingId) {
        // We need to get by ID, let me add this method to the service
        return null; // TODO: Implement
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdentityTypeAttributeMappingResponse createMapping(@Valid @RequestBody CreateMappingRequest request) {
        IdentityTypeAttributeMapping mapping = mappingService.createMapping(
                request.getIdentityTypeId(),
                request.getAttributeTypeId(),
                request.getSortOrder(),
                request.isRequired(),
                request.getOverrideValidationRegex(),
                request.getOverrideDefaultValue()
        );
        return mapToResponse(mapping);
    }
    
    @PutMapping("/{mappingId}")
    public IdentityTypeAttributeMappingResponse updateMapping(
            @PathVariable String mappingId, 
            @Valid @RequestBody UpdateMappingRequest request) {
        
        IdentityTypeAttributeMapping mapping = mappingService.updateMapping(
                mappingId,
                request.getSortOrder(),
                request.isRequired(),
                request.getOverrideValidationRegex(),
                request.getOverrideDefaultValue()
        );
        return mapToResponse(mapping);
    }
    
    @DeleteMapping("/{mappingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMapping(@PathVariable String mappingId) {
        mappingService.deleteMapping(mappingId);
    }
    
    @PostMapping("/{mappingId}/validate")
    public ValidationResponse validateValue(@PathVariable String mappingId, @RequestBody ValueRequest request) {
        boolean isValid = mappingService.validateValueForMapping(mappingId, request.getValue());
        String effectiveRegex = mappingService.getEffectiveValidationRegex(mappingId);
        return new ValidationResponse(isValid, effectiveRegex);
    }
    
    private IdentityTypeAttributeMappingResponse mapToResponse(IdentityTypeAttributeMapping mapping) {
        return new IdentityTypeAttributeMappingResponse(
                mapping.getId(),
                mapping.getIdentityType().getId(),
                mapping.getIdentityType().getName(),
                mapping.getAttributeType().getId(),
                mapping.getAttributeType().getName(),
                mapping.getAttributeType().getDisplayName(),
                mapping.getAttributeType().getDescription(),
                mapping.getAttributeType().getDataType().name(),
                mapping.getSortOrder(),
                mapping.isRequired(),
                mapping.getOverrideValidationRegex(),
                mapping.getOverrideDefaultValue(),
                mapping.isActive(),
                mapping.getCreatedAt(),
                mapping.getUpdatedAt(),
                mapping.getEffectiveValidationRegex(),
                mapping.getEffectiveDefaultValue(),
                mapping.getAttributeType().getValidationRegex(),
                mapping.getAttributeType().getDefaultValue()
        );
    }
    
    @Data
    public static class ValueRequest {
        private String value;
    }
    
    @Data
    @AllArgsConstructor
    public static class ValidationResponse {
        private boolean valid;
        private String effectiveRegex;
    }
}
