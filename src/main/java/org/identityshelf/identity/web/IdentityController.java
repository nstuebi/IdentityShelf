package org.identityshelf.identity.web;

import jakarta.validation.Valid;
import java.util.UUID;
import org.identityshelf.identity.service.IdentityService;
import org.identityshelf.identity.web.dto.CreateIdentityRequest;
import org.identityshelf.identity.web.dto.IdentityResponse;
import org.identityshelf.identity.web.dto.UpdateIdentityRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/identities")
public class IdentityController {

    private static final Logger logger = LoggerFactory.getLogger(IdentityController.class);

    private final IdentityService identityService;

    public IdentityController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdentityResponse create(@Valid @RequestBody CreateIdentityRequest request) {
        logger.info("Received create identity request: {}", request);
        try {
            IdentityResponse response = identityService.createIdentity(request);
            logger.info("Successfully created identity with ID: {}", response.getId());
            return response;
        } catch (Exception e) {
            logger.error("Error in create identity endpoint: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    public Page<IdentityResponse> list(@PageableDefault(size = 20) Pageable pageable) {
        return identityService.listIdentities(pageable);
    }

    @GetMapping("/{id}")
    public IdentityResponse get(@PathVariable UUID id) {
        return identityService.getIdentity(id);
    }

    @PutMapping("/{id}")
    public IdentityResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateIdentityRequest request) {
        return identityService.updateIdentity(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        identityService.deleteIdentity(id);
    }
}


