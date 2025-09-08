package org.identityshelf.adminapi.web;

import org.identityshelf.core.domain.AttributeDataType;
import org.identityshelf.core.domain.IdentifierType;
import org.identityshelf.adminapi.service.IdentifierTypeService;
import org.identityshelf.adminapi.web.dto.CreateIdentifierTypeRequest;
import org.identityshelf.adminapi.web.dto.IdentifierTypeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/identifier-types")
public class IdentifierTypeController {

    private static final Logger log = LoggerFactory.getLogger(IdentifierTypeController.class);
    private final IdentifierTypeService identifierTypeService;
    
    public IdentifierTypeController(IdentifierTypeService identifierTypeService) {
        this.identifierTypeService = identifierTypeService;
    }

    @GetMapping
    public ResponseEntity<List<IdentifierTypeResponse>> getAllIdentifierTypes() {
        List<IdentifierType> identifierTypes = identifierTypeService.getAllIdentifierTypes();
        List<IdentifierTypeResponse> response = identifierTypes.stream()
                .map(IdentifierTypeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/searchable")
    public ResponseEntity<List<IdentifierTypeResponse>> getSearchableIdentifierTypes() {
        List<IdentifierType> identifierTypes = identifierTypeService.getSearchableIdentifierTypes();
        List<IdentifierTypeResponse> response = identifierTypes.stream()
                .map(IdentifierTypeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdentifierTypeResponse> getIdentifierType(@PathVariable String id) {
        IdentifierType identifierType = identifierTypeService.getIdentifierType(id);
        return ResponseEntity.ok(IdentifierTypeResponse.from(identifierType));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<IdentifierTypeResponse> getIdentifierTypeByName(@PathVariable String name) {
        IdentifierType identifierType = identifierTypeService.getIdentifierTypeByName(name);
        return ResponseEntity.ok(IdentifierTypeResponse.from(identifierType));
    }

    @PostMapping
    public ResponseEntity<IdentifierTypeResponse> createIdentifierType(@RequestBody CreateIdentifierTypeRequest request) {
        try {
            IdentifierType identifierType = identifierTypeService.createIdentifierType(request);
            
            log.info("Successfully created identifier type with ID: {}", identifierType.getUuid());
            return ResponseEntity.status(HttpStatus.CREATED).body(IdentifierTypeResponse.from(identifierType));
        } catch (Exception e) {
            log.error("Error creating identifier type: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<IdentifierTypeResponse> updateIdentifierType(
            @PathVariable String id, 
            @RequestBody CreateIdentifierTypeRequest request) {
        try {
            IdentifierType identifierType = identifierTypeService.updateIdentifierType(id, request);
            
            log.info("Successfully updated identifier type with ID: {}", id);
            return ResponseEntity.ok(IdentifierTypeResponse.from(identifierType));
        } catch (Exception e) {
            log.error("Error updating identifier type {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIdentifierType(@PathVariable String id) {
        try {
            identifierTypeService.deleteIdentifierType(id);
            log.info("Successfully deleted identifier type with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting identifier type {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateIdentifierType(@PathVariable String id) {
        try {
            identifierTypeService.activateIdentifierType(id);
            log.info("Successfully activated identifier type with ID: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error activating identifier type {}: {}", id, e.getMessage());
            throw e;
        }
    }
}
