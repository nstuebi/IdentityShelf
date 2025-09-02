package org.identityshelf.identity.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.identityshelf.identity.domain.AttributeDataType;
import org.identityshelf.identity.domain.IdentifierType;
import org.identityshelf.identity.service.IdentifierTypeService;
import org.identityshelf.identity.web.dto.CreateIdentifierTypeRequest;
import org.identityshelf.identity.web.dto.IdentifierTypeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/identifier-types")
@RequiredArgsConstructor
@Slf4j
public class IdentifierTypeController {

    private final IdentifierTypeService identifierTypeService;

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
            IdentifierType identifierType = identifierTypeService.createIdentifierType(
                    request.getName(),
                    request.getDisplayName(),
                    request.getDescription(),
                    AttributeDataType.valueOf(request.getDataType()),
                    request.getValidationRegex(),
                    request.getDefaultValue(),
                    request.isUnique(),
                    request.isSearchable()
            );
            
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
            IdentifierType identifierType = identifierTypeService.updateIdentifierType(
                    id,
                    request.getName(),
                    request.getDisplayName(),
                    request.getDescription(),
                    AttributeDataType.valueOf(request.getDataType()),
                    request.getValidationRegex(),
                    request.getDefaultValue(),
                    request.isUnique(),
                    request.isSearchable()
            );
            
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
