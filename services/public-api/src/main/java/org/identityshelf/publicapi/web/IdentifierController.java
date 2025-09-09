package org.identityshelf.publicapi.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/identifiers")
@Tag(name = "Identifiers", description = "Identifier management operations")
public class IdentifierController {
    
}
