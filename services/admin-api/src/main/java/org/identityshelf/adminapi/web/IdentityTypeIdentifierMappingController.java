package org.identityshelf.adminapi.web;

import org.identityshelf.core.domain.IdentityTypeIdentifierMapping;
import org.identityshelf.adminapi.service.IdentityTypeIdentifierMappingService;
import org.identityshelf.adminapi.web.dto.IdentityTypeIdentifierMappingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/identity-type-identifier-mappings")
public class IdentityTypeIdentifierMappingController {

    private static final Logger log = LoggerFactory.getLogger(IdentityTypeIdentifierMappingController.class);
    private final IdentityTypeIdentifierMappingService mappingService;
public IdentityTypeIdentifierMappingController(IdentityTypeIdentifierMappingService mappingService) {
        this.mappingService = mappingService;
    }

    @GetMapping
    public ResponseEntity<List<IdentityTypeIdentifierMappingResponse>> getAllMappings() {
        log.debug("Getting all identity type identifier mappings");
        List<IdentityTypeIdentifierMapping> mappings = mappingService.getAllActiveMappings();
        List<IdentityTypeIdentifierMappingResponse> response = mappings.stream()
                .map(IdentityTypeIdentifierMappingResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-identity-type/{identityTypeId}")
    public ResponseEntity<List<IdentityTypeIdentifierMappingResponse>> getMappingsByIdentityType(
            @PathVariable UUID identityTypeId) {
        log.debug("Getting identifier mappings for identity type: {}", identityTypeId);
        List<IdentityTypeIdentifierMapping> mappings = mappingService.getMappingsByIdentityType(identityTypeId);
        List<IdentityTypeIdentifierMappingResponse> response = mappings.stream()
                .map(IdentityTypeIdentifierMappingResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdentityTypeIdentifierMappingResponse> getMapping(@PathVariable UUID id) {
        log.debug("Getting identifier mapping: {}", id);
        IdentityTypeIdentifierMapping mapping = mappingService.getMappingById(id);
        return ResponseEntity.ok(IdentityTypeIdentifierMappingResponse.from(mapping));
    }

    @PostMapping
    public ResponseEntity<IdentityTypeIdentifierMappingResponse> createMapping(
            @RequestBody CreateIdentityTypeIdentifierMappingRequest request) {
        log.debug("Creating identifier mapping for identity type: {} and identifier type: {}", 
                request.getIdentityTypeId(), request.getIdentifierTypeId());
        
        IdentityTypeIdentifierMapping mapping = mappingService.createMapping(
                request.getIdentityTypeId(),
                request.getIdentifierTypeId(),
                request.getSortOrder(),
                request.isRequired(),
                request.isPrimaryCandidate(),
                request.getOverrideValidationRegex(),
                request.getOverrideDefaultValue()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(IdentityTypeIdentifierMappingResponse.from(mapping));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IdentityTypeIdentifierMappingResponse> updateMapping(
            @PathVariable UUID id,
            @RequestBody UpdateIdentityTypeIdentifierMappingRequest request) {
        log.debug("Updating identifier mapping: {}", id);
        
        IdentityTypeIdentifierMapping mapping = mappingService.updateMapping(
                id,
                request.getSortOrder(),
                request.isRequired(),
                request.isPrimaryCandidate(),
                request.getOverrideValidationRegex(),
                request.getOverrideDefaultValue()
        );
        
        return ResponseEntity.ok(IdentityTypeIdentifierMappingResponse.from(mapping));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMapping(@PathVariable UUID id) {
        log.debug("Deleting identifier mapping: {}", id);
        mappingService.deactivateMapping(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<IdentityTypeIdentifierMappingResponse> activateMapping(@PathVariable UUID id) {
        log.debug("Activating identifier mapping: {}", id);
        IdentityTypeIdentifierMapping mapping = mappingService.activateMapping(id);
        return ResponseEntity.ok(IdentityTypeIdentifierMappingResponse.from(mapping));
    }

    // Request DTOs
    public static class CreateIdentityTypeIdentifierMappingRequest {
        private UUID identityTypeId;
        private UUID identifierTypeId;
        private int sortOrder;
        private boolean required;
        private boolean primaryCandidate;
        private String overrideValidationRegex;
        private String overrideDefaultValue;

        // Getters and setters
        public UUID getIdentityTypeId() { return identityTypeId; }
        public void setIdentityTypeId(UUID identityTypeId) { this.identityTypeId = identityTypeId; }

        public UUID getIdentifierTypeId() { return identifierTypeId; }
        public void setIdentifierTypeId(UUID identifierTypeId) { this.identifierTypeId = identifierTypeId; }

        public int getSortOrder() { return sortOrder; }
        public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }

        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }

        public boolean isPrimaryCandidate() { return primaryCandidate; }
        public void setPrimaryCandidate(boolean primaryCandidate) { this.primaryCandidate = primaryCandidate; }

        public String getOverrideValidationRegex() { return overrideValidationRegex; }
        public void setOverrideValidationRegex(String overrideValidationRegex) { this.overrideValidationRegex = overrideValidationRegex; }

        public String getOverrideDefaultValue() { return overrideDefaultValue; }
        public void setOverrideDefaultValue(String overrideDefaultValue) { this.overrideDefaultValue = overrideDefaultValue; }
    }

    public static class UpdateIdentityTypeIdentifierMappingRequest {
        private int sortOrder;
        private boolean required;
        private boolean primaryCandidate;
        private String overrideValidationRegex;
        private String overrideDefaultValue;

        // Getters and setters
        public int getSortOrder() { return sortOrder; }
        public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }

        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }

        public boolean isPrimaryCandidate() { return primaryCandidate; }
        public void setPrimaryCandidate(boolean primaryCandidate) { this.primaryCandidate = primaryCandidate; }

        public String getOverrideValidationRegex() { return overrideValidationRegex; }
        public void setOverrideValidationRegex(String overrideValidationRegex) { this.overrideValidationRegex = overrideValidationRegex; }

        public String getOverrideDefaultValue() { return overrideDefaultValue; }
        public void setOverrideDefaultValue(String overrideDefaultValue) { this.overrideDefaultValue = overrideDefaultValue; }
    }
}
