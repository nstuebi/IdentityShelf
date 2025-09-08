package org.identityshelf.adminapi.web;

import org.identityshelf.adminapi.service.IdentityTypeService;
import org.identityshelf.adminapi.web.dto.IdentityTypeResponse;
import org.identityshelf.adminapi.web.dto.AttributeTypeResponse;
import org.identityshelf.core.domain.IdentityType;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/identity-types")
public class IdentityTypeController {
    
    private static final Logger logger = LoggerFactory.getLogger(IdentityTypeController.class);
    
    private final IdentityTypeService identityTypeService;
    
    public IdentityTypeController(IdentityTypeService identityTypeService) {
        this.identityTypeService = identityTypeService;
    }
    
    @GetMapping
    public List<IdentityTypeResponse> getAllTypes() {
        logger.info("Fetching all identity types");
        List<IdentityType> types = identityTypeService.getAllActiveTypes();
        return types.stream()
                .map(type -> new IdentityTypeResponse(
                        type.getUuid().toString(),
                        type.getName(),
                        type.getDisplayName(),
                        type.getDescription(),
                        type.isActive(),
                        type.getCreatedAt(),
                        type.getUpdatedAt(),
                        List.of() // attributes - TODO: implement
                ))
                .collect(Collectors.toList());
    }
    
    @GetMapping("/{typeName}")
    public IdentityTypeResponse getTypeByName(@PathVariable String typeName) {
        logger.info("Fetching identity type: {}", typeName);
        IdentityType type = identityTypeService.getTypeByName(typeName);
        if (type != null) {
            return new IdentityTypeResponse(
                    type.getUuid().toString(),
                    type.getName(),
                    type.getDisplayName(),
                    type.getDescription(),
                    type.isActive(),
                    type.getCreatedAt(),
                    type.getUpdatedAt(),
                    List.of() // attributes - TODO: implement
            );
        } else {
            return null;
        }
    }
    
    @GetMapping("/{typeName}/attributes")
    public List<AttributeTypeResponse> getAttributesForType(@PathVariable String typeName) {
        logger.info("Fetching attributes for identity type: {}", typeName);
        List<Object> attributes = identityTypeService.getAttributesForType(typeName);
        return attributes.stream()
                .map(attr -> new AttributeTypeResponse(
                        "unknown", // TODO: implement proper mapping
                        "unknown",
                        "unknown",
                        "unknown",
                        "STRING",
                        false,
                        "",
                        ".*",
                        0,
                        true,
                        null,
                        null
                ))
                .collect(Collectors.toList());
    }
}
