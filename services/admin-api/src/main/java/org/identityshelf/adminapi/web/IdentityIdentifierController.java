package org.identityshelf.adminapi.web;

import org.identityshelf.core.domain.IdentityIdentifier;
import org.identityshelf.adminapi.service.IdentityIdentifierService;
import org.identityshelf.adminapi.web.dto.CreateIdentifierRequest;
import org.identityshelf.adminapi.web.dto.IdentityIdentifierResponse;
import org.identityshelf.adminapi.web.dto.IdentityIdentifierSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/identifiers")
public class IdentityIdentifierController {

    private static final Logger log = LoggerFactory.getLogger(IdentityIdentifierController.class);
    private final IdentityIdentifierService identifierService;
    
    public IdentityIdentifierController(IdentityIdentifierService identifierService) {
        this.identifierService = identifierService;
    }

    // Search operations
    @GetMapping("/search")
    public ResponseEntity<List<IdentityIdentifierResponse>> searchIdentifiers(@RequestParam String value) {
        List<IdentityIdentifier> identifiers = identifierService.findIdentitiesByIdentifierValue(value);
        List<IdentityIdentifierResponse> response = identifiers.stream()
                .map(IdentityIdentifierResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<List<IdentityIdentifierResponse>> searchIdentifiers(@RequestBody IdentityIdentifierSearchRequest request) {
        UUID typeUuid = request.getIdentifierTypeId() != null ? UUID.fromString(request.getIdentifierTypeId()) : null;
        List<IdentityIdentifier> identifiers = identifierService.searchIdentifiers(request);
        List<IdentityIdentifierResponse> response = identifiers.stream()
                .map(IdentityIdentifierResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/suggestions")
    public ResponseEntity<List<IdentityIdentifierResponse>> getIdentifierSuggestions(@RequestParam String partial) {
        List<IdentityIdentifier> identifiers = identifierService.findIdentifierSuggestions(partial);
        List<IdentityIdentifierResponse> response = identifiers.stream()
                .map(IdentityIdentifierResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/by-type-and-value")
    public ResponseEntity<IdentityIdentifierResponse> findByTypeAndValue(
            @RequestParam String typeName, 
            @RequestParam String value) {
        IdentityIdentifier identifier = identifierService.findIdentityByTypeAndValue(typeName, value);
        if (identifier != null) {
            return ResponseEntity.ok(IdentityIdentifierResponse.from(identifier));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Identity-specific operations
    @GetMapping("/identity/{identityId}")
    public ResponseEntity<List<IdentityIdentifierResponse>> getIdentifiersForIdentity(@PathVariable UUID identityId) {
        List<IdentityIdentifier> identifiers = identifierService.getIdentifiersForIdentity(identityId);
        List<IdentityIdentifierResponse> response = identifiers.stream()
                .map(IdentityIdentifierResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/identity/{identityId}/primary")
    public ResponseEntity<IdentityIdentifierResponse> getPrimaryIdentifierForIdentity(@PathVariable UUID identityId) {
        IdentityIdentifier identifier = identifierService.getPrimaryIdentifierForIdentity(identityId);
        if (identifier != null) {
            return ResponseEntity.ok(IdentityIdentifierResponse.from(identifier));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/identity/{identityId}/verified")
    public ResponseEntity<List<IdentityIdentifierResponse>> getVerifiedIdentifiersForIdentity(@PathVariable UUID identityId) {
        List<IdentityIdentifier> identifiers = identifierService.getVerifiedIdentifiersForIdentity(identityId);
        List<IdentityIdentifierResponse> response = identifiers.stream()
                .map(IdentityIdentifierResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // CRUD operations
    @PostMapping
    public ResponseEntity<IdentityIdentifierResponse> createIdentifier(@RequestBody CreateIdentifierRequest request) {
        try {
            IdentityIdentifier identifier = identifierService.addIdentifierToIdentity(
                    UUID.fromString(request.getIdentityId()),
                    UUID.fromString(request.getIdentifierTypeId()),
                    request.getIdentifierValue(),
                    request.isPrimary()
            );
            
            log.info("Successfully created identifier with ID: {}", identifier.getUuid());
            return ResponseEntity.status(HttpStatus.CREATED).body(IdentityIdentifierResponse.from(identifier));
        } catch (Exception e) {
            log.error("Error creating identifier: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<IdentityIdentifierResponse> updateIdentifier(
            @PathVariable UUID id,
            @RequestParam String identifierValue,
            @RequestParam(defaultValue = "false") boolean primary) {
        try {
            IdentityIdentifier identifier = identifierService.updateIdentifier(id, identifierValue, primary);
            log.info("Successfully updated identifier with ID: {}", id);
            return ResponseEntity.ok(IdentityIdentifierResponse.from(identifier));
        } catch (Exception e) {
            log.error("Error updating identifier {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIdentifier(@PathVariable UUID id) {
        try {
            identifierService.deleteIdentifier(id);
            log.info("Successfully deleted identifier with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting identifier {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @PostMapping("/{id}/verify")
    public ResponseEntity<IdentityIdentifierResponse> verifyIdentifier(
            @PathVariable UUID id,
            @RequestParam String verifiedBy) {
        try {
            IdentityIdentifier identifier = identifierService.verifyIdentifier(id, verifiedBy);
            log.info("Successfully verified identifier with ID: {}", id);
            return ResponseEntity.ok(IdentityIdentifierResponse.from(identifier));
        } catch (Exception e) {
            log.error("Error verifying identifier {}: {}", id, e.getMessage());
            throw e;
        }
    }

    // Statistics
    @GetMapping("/statistics/count-by-type/{typeId}")
    public ResponseEntity<Long> getIdentifierCountByType(@PathVariable UUID typeId) {
        long count = identifierService.getIdentifierCountByType(typeId);
        return ResponseEntity.ok(count);
    }
}
