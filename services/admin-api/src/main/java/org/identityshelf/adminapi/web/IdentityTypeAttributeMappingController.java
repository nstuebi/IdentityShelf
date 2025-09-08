package org.identityshelf.adminapi.web;

import org.identityshelf.core.domain.IdentityTypeAttributeMapping;
import org.identityshelf.adminapi.service.IdentityTypeAttributeMappingService;
import org.identityshelf.adminapi.web.dto.CreateMappingRequest;
import org.identityshelf.adminapi.web.dto.IdentityTypeAttributeMappingResponse;
import org.identityshelf.adminapi.web.dto.UpdateMappingRequest;
import org.identityshelf.adminapi.web.dto.ValueRequest;
import org.identityshelf.adminapi.web.dto.ValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/identity-type-attribute-mappings")
public class IdentityTypeAttributeMappingController {
    
    private final IdentityTypeAttributeMappingService mappingService;
public IdentityTypeAttributeMappingController(IdentityTypeAttributeMappingService mappingService) {
        this.mappingService = mappingService;
    }
    
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
        IdentityTypeAttributeMapping mapping = mappingService.getMappingById(mappingId);
        return mapToResponse(mapping);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdentityTypeAttributeMappingResponse createMapping(@Valid @RequestBody CreateMappingRequest request) {
        IdentityTypeAttributeMapping mapping = mappingService.createMapping(request);
        return mapToResponse(mapping);
    }
    
    @PutMapping("/{mappingId}")
    public IdentityTypeAttributeMappingResponse updateMapping(
            @PathVariable String mappingId, 
            @Valid @RequestBody UpdateMappingRequest request) {
        
        IdentityTypeAttributeMapping mapping = mappingService.updateMapping(UUID.fromString(mappingId), request);
        return mapToResponse(mapping);
    }
    
    @DeleteMapping("/{mappingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMapping(@PathVariable String mappingId) {
        mappingService.deleteMapping(UUID.fromString(mappingId));
    }
    
    @PostMapping("/{mappingId}/validate")
    public ValidationResponse validateValue(@PathVariable String mappingId, @RequestBody ValueRequest request) {
        boolean isValid = mappingService.validateValueForMapping(mappingId, request.getValue());
        String effectiveRegex = mappingService.getEffectiveValidationRegex(mappingId);
        return new ValidationResponse(isValid, effectiveRegex);
    }
    
    private IdentityTypeAttributeMappingResponse mapToResponse(IdentityTypeAttributeMapping mapping) {
        return IdentityTypeAttributeMappingResponse.from(mapping);
    }
    
    
}
