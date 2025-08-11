package org.identityshelf.identity.web;

import org.identityshelf.identity.service.IdentityTypeService;
import org.identityshelf.identity.web.dto.IdentityTypeResponse;
import org.identityshelf.identity.web.dto.AttributeTypeResponse;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        return identityTypeService.getAllActiveTypes();
    }
    
    @GetMapping("/{typeName}")
    public IdentityTypeResponse getTypeByName(@PathVariable String typeName) {
        logger.info("Fetching identity type: {}", typeName);
        return identityTypeService.getTypeByName(typeName);
    }
    
    @GetMapping("/{typeName}/attributes")
    public List<AttributeTypeResponse> getAttributesForType(@PathVariable String typeName) {
        logger.info("Fetching attributes for identity type: {}", typeName);
        return identityTypeService.getAttributesForType(typeName);
    }
}
